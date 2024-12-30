public class Main {
    public static void main(String[] args) {
        FifoTemplate<Character> s = new FifoTemplate();//nie mozna konkretyzowac szablonu typen podstawwoywm
        FifoTemplate<Integer> s1 = new FifoTemplate<>();
        try{
            s.PQEnqueue('A');
            s.PQEnqueue('B');
            s.PQEnqueue('C');
            s1.PQEnqueue(1);
            s1.PQEnqueue(2);
            s1.PQEnqueue(3);



            System.out.println(s.PQDequeue()+ " "+ s.PQDequeue());
            System.out.println(s.PQDequeue());
            //System.out.println(s.pop());

            System.out.println(s1.PQDequeue()+ " "+ s1.PQDequeue());
            System.out.println(s1.PQDequeue());
            System.out.println(s1.PQDequeue());
        }catch (FifoException e){
            System.out.println(e.getReason());
        }
    }
}