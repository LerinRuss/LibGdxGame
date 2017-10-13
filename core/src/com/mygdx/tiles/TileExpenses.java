package com.mygdx.tiles;

import com.mygdx.exceptions.runtime.GameLogicException;

/**
 * describe the points one need to spend to get through a appropriate tile.
 * It also keeps a minimum of expenses other than zero. if minimum expense is zero this means all expenses are zero
 * and it is not modified.
 * Also it keeps maximum expense.
 *
 * Описывает очки необходимые для того, чтобы пройти соответствующий тайл.
 * Этот класс также хранит минимальный и максимальный очки затраты. Минимальные затраты хранятся, отличные от нуля.
 * Если минимальные затраты равны нулю, это значит, что не были заданны (модифицированы) затраты.
 */
public class TileExpenses {
    private int[] expenses = new int[TileType.values().length];
    private int minExpense, maxExpense;

    public void setExpense(TileType tileType, int expense) {
        setExpense(tileType.ordinal(), expense);
    }

    public void setExpense(int index, int expense){
        if(expense < 0)
            throw new GameLogicException("Tile cannot have a negative expense for passing of tile");
        if(expense != 0 && expense < minExpense)
            minExpense = expense;
        else if(expense > maxExpense)
            maxExpense = expense;

        expenses[index] = expense;
    }

    public int getExpense(TileType tileType) {
        return getExpense(tileType.ordinal());
    }

    public int getExpense(int index){
        return expenses[index];
    }

    public int getMinExpense() {
        return minExpense;
    }

    public int getMaxExpense() {
        return maxExpense;
    }
}
