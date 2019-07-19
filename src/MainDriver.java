import java.util.Random;

/**
 * author: copypasteearth
 * date: 7/18/2019
 * project: BalancedBinarySearchTree
 */
public class MainDriver {


    public static void main(String[] args){
        AVLSearchTree<Integer,String> tree = new AVLSearchTree<>();
        int size = 10000000;
        //String[] testing = new String[size];
        long start = System.currentTimeMillis();
        PrintVisitor printVisitor = new PrintVisitor();
        for(int i = 0;i < size;i++){
            String enter = i+"";
           tree.insert(i,enter);
           //testing[i] = enter;

        }
        long end = System.currentTimeMillis();
        /*printVisitor.start();
        tree.traverseInorder(printVisitor);
        printVisitor.finish();
        System.out.println();
        ValidationVisitor validator = new ValidationVisitor(size,
                testing);
        tree.traverseInorder(validator);
        if (validator.isValid())
            System.out.print("Passed: ");
        else
            System.out.print("Failed: ");
        System.out.println();
        //System.out.println("tree size: " + tree.getSize());
        //System.out.println(tree);

        */
        System.out.println("Balanced Tree with " + size + " elements took: " + (end - start) + " ms");
        Random random = new Random();
        int selected = random.nextInt(size);
        System.out.println("key: " + selected);
        long start1 = System.currentTimeMillis();
        String a = tree.find(selected);
        long end1 = System.currentTimeMillis();
        System.out.println("Balanced Tree find random element "+a+ " of size " + size + " elements took: " + (end1 - start1) + " ms");

        //System.out.println(tree);

    }
}
