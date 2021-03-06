import java.util.ArrayList;

public class Application {
    public ArrayList<AbstractUser> userList;

    public Application(){
        this.userList = new ArrayList<>();
    }

    public void addUser(AbstractUser user){
        userList.add(user);
    }
}