package server;

import java.util.ArrayList;
import java.util.List;

public class testData implements DataService {
	public List<Data> getDatas(){
        List<Data> newes = new ArrayList<Data>();
        newes.add(new Data("wifi1", "url1"));
        newes.add(new Data("wifi2", "url2"));
        newes.add(new Data("wifi3", "url3"));
        return newes;
    }

}
