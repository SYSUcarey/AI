import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node implements Comparable<Node>{
	String digital;                 //������״̬
    Node parent;                   	//���ڵ�
    List<String> precursor;       	//����ǰ���ڵ�
    List<Node> successor;        	//���к�̽ڵ�
    int depth;                      //�ڵ���ȣ���g(n)
    int eval;                       //�ڵ���������Ӳ�ͬ����������������h(n)
    Node(String digital, Node parent, List<String> precursor, List<Node> successor, int depth, int eval) {
        this.digital = digital;
        this.parent = parent;
        this.precursor = precursor;
        this.successor = successor;
        this.depth = depth;
        this.eval = eval;
    }

    //�ж�����İ�����״̬�Ƿ���ǰ���ڵ�
    boolean isPrecursor(String digital) {
        for (String it : precursor) {
            if (it.equals(digital)) return true;
        }
        return false;
    }

	@Override
	public int compareTo(Node o) {
		return (this.depth + this.eval) - (o.depth + o.eval);
	}
	
	//���Ժ���
	public static void main(String[] argv)  {
		List<Node> list = new ArrayList<>();
		list.add(new Node("1", null, null, null, 1, 2));
		list.add(new Node("2", null, null, null, 0, 0));
		list.add(new Node("3", null, null, null, 0, 1));
		for (Node i : list) {
			System.out.println(i.digital);
		}
		Collections.sort(list);
		for (Node i : list) {
			System.out.println(i.digital);
		}
		Node node1 = new Node("12", null, null, null, 0, 0);
		List<String> precursor = new ArrayList<>();
		precursor.add("12");
		Node node2 = new Node("1", null, precursor, null, 0, 0);
		System.out.println(node2.isPrecursor(node1.digital));
	}
}
