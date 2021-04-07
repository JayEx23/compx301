public class MergeRuns {

    public static void main(String[] args) {
        if(args.length > 1) {
            System.out.println("Usage");
            return;
        }
        try {
            DistributeRuns dr = new DistributeRuns(Integer.parseInt(args[0]));
            dr.createFile();
        } catch (Exception x) {
            System.out.println(x);
        }
        
    }
    
}
