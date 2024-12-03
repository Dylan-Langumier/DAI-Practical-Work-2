package ch.heigvd.dai.client;

public class Win extends Instruction{

    public Win(){
        super("WIN");
    }

    @Override
    protected String execute(String[] arguments) {
        System.out.println("You won.");
        return null;
    }
}
