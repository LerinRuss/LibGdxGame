package com.mygdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.control.handlers.Handler;
import com.mygdx.control.input.GameInput;

/**
 * Created by Viteker on 19.08.2017.
 */
public class GameMultiplexer {
    private final InputMultiplexer multiplexer;
    private PooledLinkedList<Handler> order = new PooledLinkedList<Handler>(Integer.MAX_VALUE);
    private Handler spare;
    private int index;

    public GameMultiplexer(InputMultiplexer multiplexer, Handler spare){
        this.multiplexer = multiplexer;
        this.spare = spare;
        index = multiplexer.size();
        multiplexer.addProcessor(new GameInput(spare));
    }

    public void addProcessor(InputProcessor processor) {
        multiplexer.addProcessor(index, processor);
        index++;
    }
    public void removeProcessor(InputProcessor processor){
        multiplexer.removeProcessor(processor);
        index--;
    }
    public void addToOrder(Handler handler){
        order.add(handler);
    }
    public void replace(){
        if(order.size() == 0){
            multiplexer.addProcessor(new GameInput(spare));
            return;
        }
        int end = multiplexer.getProcessors().size;
        for(int i = index; i < end; i++)
            multiplexer.removeProcessor(i);
        order.iter();
        Handler handler;
        while((handler = order.next()) != null)
            multiplexer.addProcessor(new GameInput(handler));
        order.clear();
    }
}
