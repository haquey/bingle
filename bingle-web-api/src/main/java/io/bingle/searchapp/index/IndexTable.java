package io.bingle.searchapp.index;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexTable{
    private HashMap<String, ArrayList<File>> table;

    public IndexTable(){
    }

    public HashMap<String, ArrayList<File>> getTable(){
        return table;
    }
}