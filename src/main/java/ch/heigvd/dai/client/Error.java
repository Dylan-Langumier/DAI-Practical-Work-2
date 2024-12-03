package ch.heigvd.dai.client;

public class Error extends Instruction {

    public Error(){
        super("ERROR");
    }

    @Override
    protected String execute(String[] arguments) {
        if(previous_instruction == null)
            throw new RuntimeException("No one expects the spanish Inquisition");
        return previous_instruction.execute(arguments);
    }
}
