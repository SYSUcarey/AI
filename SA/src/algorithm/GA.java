package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GA {
	private List<City> datas;	// ��������
	private int cityNum;	// ������Ŀ
	private int N = 100;		// ��Ⱥ��С
	private List<Individual> group;		// ��Ⱥ
	private int[] bestPath;
	private double bestPathLen;			// ���Ÿ����·������
	private double bestFitness; 		// ���Ÿ������Ӧֵ
	private int T = 64000; 			// ���ܴ���
	private double Pcross = 0.1;		// �������
	private double Pmutation = 0.005;	// �������
	private List<Individual> new_group;	// ��һ����Ⱥ
	private int L = 0;
	
	// ���Žⳤ��
	public double answer = 9352;
	

	double p;			//����p
	boolean isFinished = false;
	
	// ����������ת�̶�ѡ��
	double a = 1.1;		//����a		
	double b = 0.2;		//����b
	double[] proList;	//����ѡ�и�������

	public GA(List<City> datas) {
		this.datas = datas;
		this.cityNum = datas.size();
		group = new ArrayList<>();
		new_group = new ArrayList<>();
		initProList();
		initGroup();
		//evolution();
	}
	
	// �Ŵ��㷨�����������
	public void evolution() {
		Date begin = new Date();
		List<Individual> temp = group;
		
		while(++L < T) {
			//System.out.println("-----" + L + "-----");
			//System.out.println("-------" + L + "------" + "best: " + bestPathLen);
			//if(L%1000==999) showGroup(group);
			//if(L%10000==0) System.out.print("--" + L + "--");
			select();
			cross();
			mutation();
			Collections.sort(new_group);
			temp = group;
			group = new_group;
			new_group = temp;
			double len = group.get(0).len;
			if(len < bestPathLen) {
				bestPath = group.get(0).path;
				bestPathLen = group.get(0).len;
				bestFitness = group.get(0).fitness;
			}
		}
		isFinished = true;
		System.out.println(T);
		//showGroup(group);
		Date end = new Date();
		long dur = end.getTime() - begin.getTime();
		System.out.println("[Time] " + dur + "ms");
		System.out.println("[Result] " + bestPathLen);
		double eff = (bestPathLen/9352 - 1) * 100;
		System.out.println("[Effect] " + eff + "%");
		System.out.println("[Path] ");
		showPath(bestPath);
	}
	
	/*---------ѡ��: ���̶�ѡ��---------*/
	public void select() {
		new_group.clear();
		Individual individual;
		
		for(int i = 0; i < N/10; i++) {
			individual = group.get(i).clone();
			new_group.add(individual);
		}
		while(new_group.size() < N) {
			p = new Random().nextDouble();
			for(int j = 0; j < N; j++) {
				// ѡ�����j
				if(p <= proList[j]) {
					individual = group.get(j).clone();
					new_group.add(individual);
					break;
				}
			}
		}
		Collections.sort(new_group);
	}
	
	// �������
	public void cross() {
		//System.out.println("cross");
		//System.out.println("before");
		//showGroup(new_group);
		for(int i = 0; i < N; i++) {
			p = new Random().nextDouble();
			if(p < Pcross) {
				int type = new Random().nextInt(3);
				int p1 = new Random().nextInt(cityNum);
				int p2 = new Random().nextInt(cityNum);
				int[] path = new_group.get(i).path;
				switch(type) {
					case 0:
						Tools.swap(path, p1, p2);
					case 1:
						Tools.reverse(path, p1, p2);
					case 2:
						Tools.rightShift(path, p1, p2);
				}
				double len = getPathLen(path, datas);
				double fitness = answer / len;
				new_group.get(i).setPath(path);
				new_group.get(i).setLen(len);
				new_group.get(i).setFitness(fitness);
				
			}
		}
		//System.out.println("after");
		//showGroup(new_group);
		
	}

	// �������
	public void mutation() {
		for(int i = 0; i < N; i++) {
			p = new Random().nextDouble();
			if(p < Pmutation) {
				int[] path = Tools.create_random_permutation(cityNum);
				double len = getPathLen(path, datas);
				double fitness = answer / len;
				new_group.get(i).setPath(path);
				new_group.get(i).setLen(len);
				new_group.get(i).setFitness(fitness);
			}
		}
	}
	// ����ѡ�������ÿ����λ�����ѡ������
	public void initProList() {
		proList = new double[cityNum];
		double temp = 0;
		for(int i = 0; i < N; i++) {
			temp += getProbability(i+1);
			proList[i] = temp;
		}
	}
	
	// ��ʼ��Ⱥ
	public void initGroup() {
		// �޲��������Ⱥ
		int[] path;
		double len;
		double fitness;
		for(int i = 0; i < N; i++) {
			path = Tools.create_random_permutation(cityNum);
			len = getPathLen(path, datas);
			fitness = answer / len;
			Individual individual = new Individual(path, len, fitness);
			group.add(individual);
		}
		// ����Ӧ�ȸ���Ⱥ����
		//showGroup();
		Collections.sort(group);
		//System.out.println("-------sort------");
		bestPath = group.get(0).path;
		bestPathLen = group.get(0).len;
		bestFitness = group.get(0).fitness;
		//showGroup();
		//System.out.println("---best: " + bestPathLen + " " + bestFitness);
	}
	
	// ����·���ܾ���
	public double getPathLen(int[] path_, List<City> data_) {
		double res = 0;
		for(int i = 0; i < path_.length; i++) {
			int p1 = path_[i] - 1;
			int p2 = path_[(i+1)%path_.length] - 1;
			res += City.getDistance(data_.get(p1), data_.get(p2));
		}
		return res;
	}
	
	public void showGroup(List<Individual> group) {
		for(int i = 0; i < N; i++) {
			System.out.println("len: " + group.get(i).len + " pro: " + getProbability(i+1));
		}
	}
	
	// ������Ⱥ����Ӧ���������õ����̶ĸ���
	public double getProbability(int i) {
		return (a - b*i/(N+1))/N;
	}
	
	// ��ȡ���·��
	public int[] getPath() {
		return bestPath;
	}
	
	public boolean getFinished() {
		return this.isFinished;
	}
	
	// չʾ·��
	public void showPath(int[] path_) {
		for(int i = 0; i < path_.length-1; i++) {
			System.out.print(String.format(path_[i] + "->"));
			//if(i % 10 == 9) System.out.println();
		}
		System.out.println(path_[path_.length-1]);
	}
	
}



