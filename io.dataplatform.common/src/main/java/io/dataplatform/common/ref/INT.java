package io.dataplatform.common.ref;

public class INT {
    private int value;


    public INT(){
        this(0);
    }
    public INT(int initValue){
        value = initValue;
    }

    public void increase(){
        ++value;
    }

    public void decrease(){
        --value;
    }

    public int getValue() {
        return value;
    }
}
