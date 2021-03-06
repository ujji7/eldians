

public class Application {
    public ArrayList<AbstractUser> userList;

    public Application(){
        this.userList = new Arraylist<>();
    }

    public void addUser(AbstractUser user){
        userList.add(user);
    }
}