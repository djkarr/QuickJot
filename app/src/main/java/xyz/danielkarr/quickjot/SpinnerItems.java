package xyz.danielkarr.quickjot;

import java.util.ArrayList;
import java.util.Arrays;

public class SpinnerItems {
    final ArrayList<String> mItems;

   public SpinnerItems(){
        mItems = new ArrayList<>(Arrays.asList(
                "General","TO-DO","Shopping","Ideas","Read/Watch","Prayer","Goals"
        ));
    }

    public ArrayList<String> getItems() {
        return mItems;
    }
}
