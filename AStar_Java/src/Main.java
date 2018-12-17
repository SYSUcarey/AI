import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main extends JFrame{
	String startStr = "283164705";
	AStar aStar;
	Stack<String> result;
	
	//�������
	JPanel jpControl, jpDigital;
    int size = 9;
    JButton jbs[] = new JButton[size];
    
    JButton randomBtn, startBtn;
    
    public JButton getJButtons(int i) {
    	return jbs[i];
    }
	
	//����ͼ�λ�����
    // ���캯��
    public Main() {
    	aStar = new AStar(startStr);
        // �������
    	jpControl = new JPanel();
    	jpDigital = new JPanel();
        for (int i = 0; i < size; i++) {
            jbs[i] = new JButton(String.valueOf(i));
            jbs[i].setBackground(Color.GRAY);					//���ð�ť����
            jbs[i].setBorderPainted(false);						//ȥ����ť�߿�
            jbs[i].setForeground(Color.WHITE);					//����������ɫ
            jbs[i].setFont(new Font("����", Font.BOLD, 32));		//����������ʽ
            jbs[i].setFocusPainted(false);						//���õ����ť�󲻳��ֽ���߿�
        }
        randomBtn = new JButton("random initialization");
        randomBtn.setPreferredSize(new Dimension(150, 30));
        randomBtn.setBackground(Color.ORANGE);
        randomBtn.setForeground(Color.BLACK);
        randomBtn.setFocusPainted(false);
        
        startBtn = new JButton("start");
        startBtn.setPreferredSize(new Dimension(150, 30));
        startBtn.setBackground(Color.ORANGE);
        startBtn.setForeground(Color.BLACK);
        startBtn.setFocusPainted(false);
        
        // �������񲼾�,����ֻ��ǰ������������/�У�3��3 �Ļ�������û�п�϶
        jpDigital.setLayout(new GridLayout(3, 3, 10, 10));
        //JPanel����Ĭ����BorderLoyout

        // ������
        this.add(jpControl, BorderLayout.SOUTH);
        this.add(jpDigital, BorderLayout.CENTER);
        for (int i = 0; i < size; i++) {
            jpDigital.add(jbs[i]);
        }
        jpControl.add(randomBtn, BorderLayout.EAST);
        jpControl.add(startBtn, BorderLayout.SOUTH);
        
        //��ӵ��������
        addClickListener();
        
        
        // ���ô�������
        this.setTitle("������");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int windowWidth = this.getWidth(); //��ô��ڿ�
        int windowHeight = this.getHeight();//��ô��ڸ�
        Toolkit kit = Toolkit.getDefaultToolkit(); //���幤�߰�
        Dimension screenSize = kit.getScreenSize(); //��ȡ��Ļ�ĳߴ�
        int screenWidth = screenSize.width; //��ȡ��Ļ�Ŀ�
        int screenHeight = screenSize.height; //��ȡ��Ļ�ĸ�
        this.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);//���ô��ھ�����ʾ

        // ��ʾ
        this.setVisible(true);
    }
    
    private void addClickListener() {
		randomBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				//������ɳ�ʼ״̬����ʾ
				StringBuilder sb = new StringBuilder(startStr);
				int count = 0;
				while (count != 5) {
					for (int i = 0; i < 9; ++i) {
						int a = (int)(Math.random() * 9);
						int b = (int)(Math.random() * 9);
						char tmp = sb.charAt(a);
						sb.setCharAt(a, sb.charAt(b));
						sb.setCharAt(b, tmp);
					}
					startStr = sb.toString();
					if (aStar.solvable(startStr)) break;
					count++;
				}
				printStr(startStr);
			}
		});
		
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//��ʼAStar�㷨��������
				aStar = new AStar(startStr);
				boolean solvable = aStar.solvable(startStr);
				if (solvable == false) {
					JOptionPane.showMessageDialog(null, "No Solution", "Info", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				result = aStar.AStarSearch(2);
				startStr = aStar.end;
				//��ʾ���
				new Thread(new Runnable() {
					@Override
					public void run() {
						randomBtn.setEnabled(false);
						startBtn.setEnabled(false);
						while (!result.empty()) {
							printStr(result.pop());
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						randomBtn.setEnabled(true);
						startBtn.setEnabled(true);
					}
				}).start();
			}
		});
	}
    
    void printStr(String str) {
    	for (int i = 0; i < 9; ++i) {
        	String text = str.substring(i, i + 1);
        	if (text.equals("0")) {
        		getJButtons(i).setBackground(Color.WHITE);
        		getJButtons(i).setText("");
        		getJButtons(i).setBorder(BorderFactory.createRaisedBevelBorder());
        	}
        	else {
        		jbs[i].setBackground(Color.GRAY);
        		getJButtons(i).setText(text);
        		getJButtons(i).setBorder(null);
        	}
       }
    }
    
    public static void main(String[] args) {
        // ����ʵ��
        Main main = new Main();
        main.printStr(main.startStr);
    }

}
