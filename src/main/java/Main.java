import com.jagrosh.jdautilities.command.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import com.jagrosh.jdautilities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId("182070555636269056");
        builder.setPrefix(".");
        builder.addCommand(new RolesCommand());
        builder.addCommand(new ManageRole());
        builder.addCommand(new addRolesAll());
        builder.addCommand(new ManagedOtherUserRole());
        builder.addCommand(new GoodBot());
        builder.addCommand(new BadBot());

        CommandClient client = builder.build();

        JDABuilder bot = new JDABuilder(AccountType.BOT);
        String token = getToken();
        bot.setToken(token);
        bot.addEventListener(client);
        bot.build();
    }

    private static String getToken(){
        String token = "Token not found";
        try(InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            token = properties.getProperty("token");
        } catch (IOException io) {
            io.printStackTrace();
        }
        return token;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        System.out.println("We received a message from " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay());

        if (event.getMessage().getContentRaw().equals(".ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }

        //else if (event.getMessage().getContentRaw().
    }

       public static class RolesCommand extends Command {

           private RolesCommand() {
               this.name = "roles";
               this.aliases = new String[]{"roles"};
               this.help = "Tells you your roles.";
           }

           @Override
           protected void execute(CommandEvent event) {
              List<Role> roles = event.getGuild().getMember(event.getAuthor()).getRoles();
              String rolesStr = "`" + event.getGuild().getMember(event.getAuthor()).getEffectiveName() + "`'s roles: ";
              String[] manageableRoles = {"overwatch","pictionary","pokemon","minecraft","destiny","league","fraserraiders","apex","Avenged","dragrace","weeb","lolita", "gameofthrones","wrestling","diy", "halo"};
              String manageableStr = "`" + event.getGuild().getName() + "`'s available roles: ";
              for (int i = 0; i<roles.size(); i++){
                  rolesStr = rolesStr + "`" + roles.get(i).getName() + "`, ";
              }
              event.getChannel().sendMessage(rolesStr).queue();
              for (String manageableRole : manageableRoles){
                    manageableStr = manageableStr + "`" + manageableRole + "`, ";
              }
              event.getChannel().sendMessage(manageableStr).queue();
           }
       }

        public static class addRolesAll extends Command {
            private addRolesAll() {
                this.name = "addroletoall";
                this.help = "Adds or removes a role from all users. Only for admins.";
                this.requiredRole = "admin";
            }

            @Override
            protected void execute(CommandEvent event){
                String[] args = event.getArgs().split(" ");
                String arg = args[0];
                GuildController guildController = new GuildController(event.getGuild());
                //Role userRole;

                for(Member member : event.getGuild().getMembers()){
                    if (!member.getUser().isBot()){
                        System.out.println(member.getNickname() + " was not a bot");
                        for(Role role : member.getRoles()){
                            System.out.println(member.getNickname() + " role: " + role.getName());
                            if (role.getName().equals("members")){
                                System.out.println(member.getNickname() + " is a member.");
                                for (Role argRole : event.getGuild().getRoles()){
                                    if (argRole.getName().equals(arg)){
                                        //Role userRole = argRole;
                                        System.out.println(member.getNickname() + " has role " + role.getName());
                                        System.out.println("Adding " + argRole.getName() + " to " + member.getNickname());
                                        guildController.addRolesToMember(member,argRole).queue();
                                    }
                                }

                            }
                        }
                    }
                }
            }
       }

       public static class ManagedOtherUserRole extends Command {

        private ManagedOtherUserRole() {
            this.name = "addRoleTo";
            this.aliases = new String[]{"addRole"};
            this.requiredRole = "admin";
            this.help = "Lets an admin add or remove a role from anyone.";
        }

        @Override
        protected void execute(CommandEvent event) {
            String[] args = event.getArgs().split(";");
            String[] roleArgs = args[0].split(" ");
            String nickname = args[args.length - 1];
            GuildController guildController = new GuildController(event.getGuild());

            Member managedMember = event.getMember();

            for (Member member : event.getGuild().getMembers()){
                System.out.println("Member:" + member.getNickname());
                System.out.println("Nickname:" + nickname);
                if (member.getNickname().toLowerCase().equals(nickname.toLowerCase())){
                    managedMember = member;
                    //event.getChannel().sendMessage("Found " + member.getNickname()).queue();
                    event.getChannel().sendMessage("The member you want to manage roles for is " + managedMember.getNickname()).queue();
                }
            }

            event.getChannel().sendMessage("Couldn't find " + nickname).queue();

            //return;

//            System.out.println("Nickname: " + nickname);
//            for (String role : roleArgs){
//                System.out.println("Role: " + role);
//            }
        }

       }

       public static class ManageRole extends Command {


            private ManageRole() {
                this.name = "role";
                this.aliases = new String[]{};
                this.help = "Lets you add or remove a role from yourself.";
                //this.requiredRole = "admin";
            }

            @Override
           protected void execute(CommandEvent event) {

                String[] manageableRoles = {"overwatch","pictionary","pokemon","minecraft","destiny","league","fraserraiders","apex","Avenged","dragrace","weeb","lolita", "gameofthrones","wrestling","diy", "halo"};
               GuildController guildController = new GuildController(event.getGuild());
               List<Role> userRoles = event.getMember().getRoles();
               List<Role> guildRoles = event.getGuild().getRoles();
               String nickname = event.getMember().getEffectiveName();

               String[] argsStr = event.getArgs().split(" ");
               String role = argsStr[0];

               if (event.getArgs().length() == 0){
                   event.getChannel().sendMessage("You must input a role to manage!").queue();
                }
                else {
                     for (Role requestedRole : guildRoles){
                         if(requestedRole.getName().toLowerCase().equals(role.toLowerCase())){
                             for (String manageableRole : manageableRoles){
                                 if (manageableRole.toLowerCase().equals(role.toLowerCase())){
                                     System.out.println("Matched " + manageableRole + " to " + role);
                                     for (Role userRole : userRoles){
                                         if (userRole.getName().toLowerCase().equals(role.toLowerCase())){
                                             System.out.println("Removing " + userRole.getName());
                                             guildController.removeRolesFromMember(event.getMember(),userRole).queue();
                                             event.getChannel().sendMessage("Removed `" + userRole.getName() + "` from `" + nickname + "`.").queue();
                                             return;
                                         }
                                     }
                                     for(Role roleObject : event.getGuild().getRoles()){
                                         if (roleObject.getName().toLowerCase().equals(role.toLowerCase())){
                                             System.out.println("Time to add the role " + roleObject.getName());
                                             guildController.addRolesToMember(event.getMember(),roleObject).queue();
                                             event.getChannel().sendMessage("Added `" + roleObject.getName() + "` to `" + nickname + "`.").queue();
                                             return;
                                         }
                                     }

                                 }
                             }
                             event.getChannel().sendMessage("You do not have permission to modify the role `" + role + "`.").queue();
                             return;
                         }
                     }
                     event.getChannel().sendMessage(role + " is not a role of `" + event.getGuild().getName() + "`. ").queue();
                }
            }
        }

        public static class GoodBot extends Command {

            private GoodBot(){
                this.name = "goodbot";
                this.help = "Tells the bot it is good";
            }

            @Override
            protected void execute(CommandEvent event){
                Member member = event.getMember();
                event.getChannel().sendMessage("Thanks " + member.getNickname() + "!").queue();
            }
        }

        public static class BadBot extends Command {

            private BadBot(){
                this.name = "badbot";
                this.help = "Tells the bot it is bad :(";
            }

            @Override
            protected void execute(CommandEvent event){
                event.getChannel().sendMessage((event.getGuild().getEmoteById("427657120876593153")).getAsMention()).queue();
            }
        }
}
