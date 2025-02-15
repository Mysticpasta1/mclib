package mchorse.mclib.math.functions;

import mchorse.mclib.math.Constant;
import mchorse.mclib.math.IValue;

/**
 * Abstract function class
 * 
 * This class provides function capability (i.e. giving it arguments and 
 * upon {@link #get()} method you receive output).
 */
public abstract class Function implements IValue
{
    protected IValue[] args;
    protected String name;

    protected IValue result = new Constant(0);

    public Function(IValue[] values, String name) throws Exception
    {
        if (values.length < this.getRequiredArguments())
        {
            String message = String.format("Function '%s' requires at least %s arguments. %s are given!", this.getName(), this.getRequiredArguments(), values.length);

            throw new Exception(message);
        }

        this.args = values;
        this.name = name;
    }

    /**
     * Get the value of nth argument 
     */
    public IValue getArg(int index)
    {
        if (index < 0 || index >= this.args.length)
        {
            throw new IllegalStateException("Index should be within the argument's length range! Given " + index + ", arguments length: " +this.args.length);
        }

        return this.args[index].get();
    }

    @Override
    public String toString()
    {
        String args = "";

        for (int i = 0; i < this.args.length; i++)
        {
            args += this.args[i].toString();

            if (i < this.args.length - 1)
            {
                args += ", ";
            }
        }

        return this.getName() + "(" + args + ")";
    }

    /**
     * Get name of this function 
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get minimum count of arguments this function needs
     */
    public int getRequiredArguments()
    {
        return 0;
    }
}