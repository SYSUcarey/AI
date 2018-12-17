import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class AStar {
	String start;
	String end;
	public AStar(String start, String end) {
		this.start = start;
		this.end = end;
	}
	
	public AStar(String start) {
		this.start = start;
		this.end = "123804765";
	}
	
	public AStar() {
		this.start = "283164705";
		this.end = "123804765";
	}
	
	//�жϰ����������Ƿ��н�
	/* һ��״̬��ʾ��һά����ʽ�������0֮���������ֵ�������֮�ͣ�
	Ҳ����ÿ������ǰ�����������ֵĸ����ĺͣ���Ϊ���״̬������

	������״̬��������ż�� ��ͬ������໥������򲻿��໥��� 
	*/
	boolean solvable(String digital) {
	    int count1 = 0, count2 = 0;
	    for (int i = 0; i < 9; ++i) {
	        for (int j = 0; j < i; ++j) {
	            if (digital.charAt(i) != '0' && digital.charAt(j) > digital.charAt(i)) 
	                count1++;
	            if (end.charAt(i) != '0' && end.charAt(j) > end.charAt(i))
	                count2++;
	        }
	    }
	    return (count1 % 2 == count2 % 2);
	}
	
	//���������ʽ�Ľڵ�İ�����״̬
	void print(String digital) {
	    for (int i = 0; i < 9; ++i) {
	        if (i % 3 == 0) System.out.println(); 
	        System.out.print(digital.charAt(i) + " ");
	    }
	    System.out.println();
	}
	
	//��������1���ԷŴ�λ�õ����ָ���Ϊ����
	int evaluation1(String digital) {
	    int eval = 0;
	    //���ƷŴ�λ�õ����ָ���
	    for (int i = 0; i < 9; ++i) {
	        if (digital.charAt(i) != end.charAt(i)) eval++;
	    }
	    return eval;
	}

	//��������2���ԷŴ�λ�õ������ƶ�����ȷλ�����貽��Ϊ����
	//����Ϊ����ᡢ������֮��ľ���ֵ�ĺ�
	int evaluation2(String digital) {
	    int eval = 0;
	    for (int i = 0; i < 9; ++i) {
	        if (digital.charAt(i) != end.charAt(i)) {
	            for (int j = 0; j < 9; ++j) {
	                if (digital.charAt(i) == end.charAt(j)) {
	                    //xΪ�����꣬yΪ������
	                    int x_i = i % 3, y_i = i / 3;
	                    int x_j = j % 3, y_j = j / 3;
	                    eval += Math.abs(x_i - x_j) + Math.abs(y_i - y_j);
	                }
	            }
	        }
	    }
	    return eval;
	}
	
	//������һ�����п��ܵİ�����״̬(һ�к��)
	List<String> generateAllSuccessor(String digital) {
	    List<String> successors = new ArrayList<>();
	    int index = digital.indexOf('0');
	    //�õ�0�ĺ������������
	    int x = index % 3, y = index / 3;
	    //0����
	    if (y != 0) {
	        StringBuilder result = new StringBuilder(digital);
	        char tmp = result.charAt(index);
	        result.setCharAt(index, result.charAt(index - 3));
	        result.setCharAt(index - 3, tmp);
	        successors.add(result.toString());
	    }
	    //0����
	    if (y != 2) {
	    	StringBuilder result = new StringBuilder(digital);
	        char tmp = result.charAt(index);
	        result.setCharAt(index, result.charAt(index + 3));
	        result.setCharAt(index + 3, tmp);
	        successors.add(result.toString());
	    }
	    //0����
	    if (x != 0) {
	    	StringBuilder result = new StringBuilder(digital);
	        char tmp = result.charAt(index);
	        result.setCharAt(index, result.charAt(index - 1));
	        result.setCharAt(index - 1, tmp);
	        successors.add(result.toString());
	    }
	    //0����
	    if (x != 2) {
	    	StringBuilder result = new StringBuilder(digital);
	        char tmp = result.charAt(index);
	        result.setCharAt(index, result.charAt(index + 1));
	        result.setCharAt(index + 1, tmp);
	        successors.add(result.toString());
	    }
	    return successors;
	}
	
	//�ݹ�����ӽڵ����
	void update(List<Node> successor, int depth) {
	    for (Node node : successor) {
	        if (node.depth > depth) {
	            node.depth = depth;
	            update(node.successor, depth);
	        }
	    }
	}
	
	//���ݵõ��ɹ���·��
	Stack<String> getPath(Node n) {
	    Stack<String> path = new Stack<>();
	    while (n != null) {
	        path.push(n.digital);
	        n = n.parent;
	    }
	    return path;
	}
	
	int evaluation(int select, String digital) {
	    return (select == 1) ? evaluation1(digital) : evaluation2(digital);
	}
	
	//������ʾѡ��������������
	Stack<String> AStarSearch(int select) {
	    System.out.println("��ʼ������״̬");
	    print(start);
	    //�����ж��Ƿ��н�
	    if (solvable(start) == false) {
	        System.out.println("No Solution");
	        return null;
	    }
	    //open������δ������Ľڵ�
	    //close�����Ѿ�������Ľڵ�
	    //����ͼ�������ű����
	    List<Node> open = new ArrayList<>();
	    List<Node> close = new ArrayList<>();

	    //���ɳ�ʼ״̬
	    Node S0 = new Node(start, null, new ArrayList<>(), new ArrayList<>(), 1, evaluation(select, start));
	    open.add(S0);

	    //��ųɹ���·����ջ
	    Stack<String> path;

	    while (true) {
	        //ʧ���˳�
	        if (open.isEmpty()) {
	            System.out.println("����ʧ��");
	            return null;
	        }

	        //��open����ȡfֵ��С�Ľڵ�n
	        //n����close������open���Ƴ�
	        Collections.sort(open);
	        Node n = open.get(0);
	        open.remove(0);
	        close.add(n);
	        
	        //��n��Ŀ��״̬����ɹ��˳�
	        if (n.digital.equals(end)) {
	            System.out.println("�����ɹ�");
	            //��n��ʼ���ݵõ�·��
	            path = getPath(n);
	            break;
	        }

	        //����n��һ�к��
	        List<String> successors = generateAllSuccessor(n.digital);
	        //��̵�ǰ���ڵ�
	        List<String> precursor = new ArrayList<>();
	        for (String string : n.precursor) {
	        	precursor.add(string);
	        }
	        precursor.add(n.digital);
	        for (int i = 0; i < successors.size(); ++i) {
	        	String successor = successors.get(i);
	            //�Բ���n��ǰ���ڵ�ĺ�̽��в���
	            if (n.isPrecursor(successor) == false) {
	                boolean isInGraph = false;
	                //��open����Ѱ�Ҹú��
	                for (int j = 0; j < open.size(); ++j) {
	                	Node it = open.get(j);
	                    //�����open����
	                    if (it.digital.equals(successor)) {
	                        //��ǰ·���Ϻã��޸ĺ�̵�ָ�룬ʹ��ָ��n
	                        if (n.depth + 1 < it.depth) {
	                            //�Ӻ��ԭ�����ڵ�ĺ���б���ɾȥ������
	                        	for (Node iNode : it.parent.successor) {
	                        		if (iNode.digital.equals(successor)) {
	                        			it.parent.successor.remove(iNode);
	                        			break;
	                        		}
	                        	}       
	                            //�޸ĺ�̵ĸ��ڵ�ָ��
	                            it.parent = n;
	                            it.depth = n.depth + 1;
	                            it.precursor = precursor;
	                        }
	                        isInGraph = true;
	                        break;
	                    }
	                }
	                //��close����Ѱ�Ҹú��
	                for (int j = 0; j < close.size(); ++j) {
	                	Node it = close.get(j);
	                    //�����close����
	                    if (it.digital.equals(successor)) {
	                        //��ǰ·���Ϻã��޸ĺ�̵�ָ�룬ʹ��ָ��n
	                        if (n.depth + 1 < it.depth) {
	                            //�Ӻ��ԭ�����ڵ�ĺ���б���ɾȥ������
	                        	for (Node iNode : it.parent.successor) {
	                        		if (iNode.digital.equals(successor)) {
	                        			it.parent.successor.remove(iNode);
	                        			break;
	                        		}
	                        	}
	                            //�޸ĺ�̵ĸ��ڵ�ָ��
	                            it.parent = n;
	                            it.depth = n.depth + 1;
	                            it.precursor = precursor;
	                            //�������ӽڵ��ָ�뼰����
	                            update(it.successor, n.depth + 1);
	                        }
	                        isInGraph = true;
	                        break;
	                    }
	                }
	                //��̲���open��Ҳ����close��
	                if (isInGraph == false) {
	                    Node child = new Node(successor, n, precursor, new ArrayList<>(), n.depth + 1, evaluation(select, successor));
	                    n.successor.add(child);
	                    open.add(child);
	                }
	            }
	        }
	    }

	    //���سɹ���·��
	    return path;
	}
	public static void main(String[] argv) {
		AStar aStar = new AStar();
		/*aStar.print(aStar.start);
		System.out.println(aStar.solvable(aStar.start));
		System.out.println(aStar.evaluation1(aStar.start));
		System.out.println(aStar.evaluation2("123856074"));
		System.out.println(aStar.generateAllSuccessor("123406785"));
		
		Node node1 = new Node("12", null, null, null, 0, 0);
		Node node2 = new Node("123", node1, null, null, 0, 0);
		Stack<String> stack = aStar.getPath(node2);
		while (!stack.empty()) {
			System.out.println(stack.pop());
		}
		*/
		Stack<String> path = aStar.AStarSearch(2);
		while (!path.empty()) aStar.print(path.pop());
	}
}
