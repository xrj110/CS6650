package Client4;

import java.security.Key;
import java.util.*;

public class kru {
    public static ArrayList<int[]> k(int g[][]){
        int n=g.length;
        HashMap<Integer,Integer> set=new HashMap();
        ArrayList<int []> edge=new ArrayList<>();
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                if(g[i][j]>0){
                    edge.add(new int[]{i,j,g[i][j]});
                    if(!set.containsKey(i)){
                        set.put(i,i);
                    }
                    if(!set.containsKey(j)){
                        set.put(j,j);
                    }
                }
            }
        }

        edge.sort((a,b)->(a[2]-b[2]));
        ArrayList <int[]>ans=new ArrayList<>();
        while(ans.size()<n-1){
            int initial_size=ans.size();
            for(int i=0;i<edge.size();i++){
                int curr[]=edge.get(i);
                if(set.get(curr[0])!=set.get(curr[1])){
                    ans.add(new int[]{curr[0],curr[1]});
                    int last=set.get(curr[1]);
                    set.put(curr[1],set.get(curr[0]));
                    for(int t:set.keySet()){
                        if(set.get(t)==last){
                            set.put(t,set.get(curr[0]));
                        }
                    }
                }
            }
            if (ans.size()==initial_size){
                break;
            }
        }
        return  ans;

    }

    public static void main(String[] args) {
        int[][] G = {
                {0, 4, 0, 0, 0, 0, 0, 8, 0},
                {4, 0, 8, 0, 0, 0, 0, 11, 0},
                {0, 8, 0, 7, 0, 4, 0, 0, 2},
                {0, 0, 7, 0, 9, 14, 0, 0, 0},
                {0, 0, 0, 9, 0, 10, 0, 0, 0},
                {0, 0, 4, 14, 10, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 1, 6},
                {8, 11, 0, 0, 0, 0, 1, 0, 7},
                {0, 0, 2, 0, 0, 0, 6, 7, 0}
        };
        ArrayList<int[]> temp=k(G);
        for (int t[]:temp){
            System.out.println(Arrays.toString(t));
        }
    }

}
