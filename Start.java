import java.io.File;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import controller.*;
import entities.Tree;

public class Start {
	public static String workingDir = System.getProperty("user.dir");
	public static int attrMax;
	public static int count = 0;

	public Tree start(String args) {
		Tree dt = new Tree(++count);
		int attrCount = 0;
		int rowCount = 0;
		int[][] dataSet;
		File trainingData = new File(args);
		try {
			Scanner scanner = new Scanner(trainingData);
			String attrs[] = scanner.nextLine().split(",");
			attrCount = attrs.length;
			attrMax = attrCount;
			while (scanner.hasNextLine()) {

				scanner.nextLine();
				rowCount++;
			}
			dataSet = new int[rowCount][attrCount];
			scanner.close();
			scanner = new Scanner(trainingData);
			scanner.nextLine();

			for (int i = 0; scanner.hasNextLine(); i++) {
				String str = scanner.nextLine();
				for (int j = 0; j < attrCount; j++)
					dataSet[i][j] = Integer.parseInt(str.split(",")[j]);
			}
			/*
			 * for(int i=0; i<rowCount;i++){ for(int j=0; j<attrCount;j++)
			 * System.out.print(dataSet[i][j]+" "); System.out.println(); }
			 */
			int[] dataClass = new int[rowCount];
			for (int i = 0; i < rowCount; i++) {
				dataClass[i] = dataSet[i][attrCount - 1];
				// System.out.println(dataClass[i]);
			}
			Helper h = new Helper();
			double entropyHead = h.calcEntropy(dataClass);
			// System.out.println("entropyHead : "+entropyHead);
			dt.setDataset(dataSet);
			recurseTree(dt, rowCount, attrCount, entropyHead, attrs);
			return dt;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;

	}

	public void test(Tree dt, String args, String type,String BAP) {
		
		int attrCount;
		Tree dtemp = dt;
		File trainingData = new File(args);
		try {
			Scanner scanner = new Scanner(trainingData);
			String attrs[] = scanner.nextLine().split(",");
			attrCount = attrs.length;
			int success = 0, total = 0;
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				int line[] = new int[attrCount];
				for (int i = 0; i < attrCount; i++) {
					line[i] = Integer.parseInt(str.split(",")[i]);
				}
				int index = 0, value = 0;
				// System.out.println(str);
				while (dtemp.leftTree != null || dtemp.rightTree != null) {

					for (int i = 0; i < attrs.length; i++) {
						if (dtemp.leftTree.getAttrUsed().equals(attrs[i])) {
							index = i;
							break;
						}
					}
					if (line[index] == 0) {
						dtemp = dtemp.rightTree;
					} else {
						dtemp = dtemp.leftTree;
					}
				}
				if (dtemp.getClassObject() == line[line.length - 1]) {
					// System.out.println("Succ"+ dtemp.getClassObject()+"
					// "+line[line.length-1]);
					success++;
					total++;
				} else {
					// System.out.println(dtemp.getAttrUsed()+" "+
					// dtemp.getClassObject()+" "+line[line.length-1]);
					total++;
				}
				dtemp = dt;
			}
			dtemp = dt;

			System.out.println("Number of "+type+" instances : " + total);
			System.out.println("Number of "+type+" attributes : " + (attrCount - 1));
			System.out.println("Accuracy of the model on the "+type+" dataset"+BAP+" : " + ((double) (success / (double) total) * 100));
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int findTreeCount(Tree dt) {
		int count = 0;
		if (dt.leftTree != null || dt.rightTree != null) {
			count = findTreeCount(dt.leftTree) + findTreeCount(dt.rightTree);
		}
		return count + 1;
	}

	public int findLeafCount(Tree dt) {
		int count = 0;
		if (dt.leftTree == null && dt.rightTree == null) {
			return 1;
		} else {
			count = findLeafCount(dt.leftTree) + findLeafCount(dt.rightTree);
		}
		return count;
	}

	public Tree find(Tree cur, int val) {
		Tree result = null;
		if (cur.leftTree != null)
			result = find(cur.leftTree, val);
		if (cur.getNodeNumber() == val)
			return cur;
		if (result == null && cur.rightTree != null)
			result = find(cur.rightTree, val);
		return result;
	}

	public static void main(String args[]) {
		Start st = new Start();
		Tree dt = new Tree(), dtemp = new Tree();
		dt = st.start(args[0]);
		int attrCount;
		File trainingData = new File(args[0]);
		try {
			Scanner scanner = new Scanner(trainingData);
			String attrs[] = scanner.nextLine().split(",");
			attrCount = attrs.length;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int treeNodes = 0, leafNodes = 0;
		treeNodes = st.findTreeCount(dt);
		leafNodes = st.findLeafCount(dt);
		System.out.println("Total number of Nodes in the tree : " + treeNodes);
		System.out.println("Number of leaf nodes in the tree : " + leafNodes);

		st.test(dt, args[0],"training","");
		System.out.println();
		st.test(dt, args[1],"validation"," before pruning");
		System.out.println();
		st.test(dt, args[2],"testing","");
		System.out.println();
		double pruneFactor = Double.parseDouble(args[3]);

		int pruneCount = (int) (pruneFactor * treeNodes);
		//System.out.println("Count: " + pruneCount);
		dtemp = dt;
		//Tree dprev = dt;
		while (pruneCount > 0) {
			Random r = new Random();
			int index = r.nextInt(treeNodes - 1)+1;
			//System.out.println(index);
			//dprev=dtemp;
			dtemp = st.find(dt, index);
			if(dtemp==null){
				//dtemp=dprev;
				pruneCount--;
				continue;
			}
			if(dtemp==dt){
				continue;
			}
			if(dtemp.leftTree==null && dtemp.rightTree==null){
				continue;
			}
			dtemp.leftTree = null;
			dtemp.rightTree = null;
			int p = 0, q = 0;
			for (int i = 0; i < dtemp.rowCount; i++) {
				if (dtemp.dataset[i][dtemp.dataset[i].length - 1] == 1)
					p++;
				else
					q++;
			}
			if (p >= q)
				dtemp.classObject = 1;
			else
				dtemp.classObject = 0;

			pruneCount--;
			
		}
		
		st.display(dt);
		treeNodes = st.findTreeCount(dt);
		leafNodes = st.findLeafCount(dt);
		System.out.println("Total number of Nodes in the tree : " + treeNodes);
		System.out.println("Number of leaf nodes in the tree : " + leafNodes);
		st.test(dt, args[0],"training","");
		System.out.println();
		st.test(dt, args[1],"validation"," after pruning");
		System.out.println();
		st.test(dt, args[2],"testing","");
		System.out.println();

	}

	public void display(Tree dt) {
		// TODO Auto-generated method stub
		if((dt.leftTree==null&&dt.rightTree==null)||dt.rowCount==0||(dt.dataset[0].length == 1)){
			System.out.println(dt.getClassObject());
			return;
		}
		int rpc = 0, rqc = 0;
		;
		for (int i = 0; i < dt.rowCount; i++) {
			if (dt.dataset[i][dt.dataset[0].length - 1] == 1)
				rpc++;
			else
				rqc++;
		}
		if (rpc == dt.rowCount) {
			dt.classObject = 1;
			System.out.println(dt.getClassObject());
			return;
		} else if (rqc == dt.rowCount) {
			dt.classObject = 0;
			System.out.println(dt.getClassObject());
			return;
		}
		System.out.println();
		for (int i = attrMax; i > dt.dataset[0].length-1; i--)
			System.out.print("|  ");
		System.out.print(dt.leftTree.getAttrUsed() + " = 1 : ");
		display(dt.leftTree);
		for (int i = attrMax; i > dt.dataset[0].length-1; i--)
			System.out.print("|  ");
		System.out.print(dt.rightTree.getAttrUsed() + " = 0 : ");
		display(dt.rightTree);
		
	}

	public static void recurseTree(Tree dt, int rowCount, int attrCount, double entropyParent, String[] attr) {
		Helper h = new Helper();
		double maxIG = 0.0;
		int attrToBeUsed = 0;

		if (rowCount == 0) {
			Random r = new Random();
			dt.setClassObject(r.nextInt(2));
			System.out.println(dt.getClassObject());
			return;
		}

		if (attrCount == 1) {
			int p = 0, q = 0;
			for (int i = 0; i < rowCount; i++) {
				if (dt.dataset[i][0] == 1)
					p++;
				else
					q++;
			}
			if (p >= q)
				dt.classObject = 1;
			else
				dt.classObject = 0;
			System.out.println(dt.getClassObject());
			return;
		}
		int rpc = 0, rqc = 0;
		;
		for (int i = 0; i < rowCount; i++) {
			if (dt.dataset[i][attrCount - 1] == 1)
				rpc++;
			else
				rqc++;
		}
		if (rpc == rowCount) {
			dt.classObject = 1;
			System.out.println(dt.getClassObject());
			return;
		} else if (rqc == rowCount) {
			dt.classObject = 0;
			System.out.println(dt.getClassObject());
			return;
		}
		System.out.println();

		for (int i = 0; i < attrCount - 1; i++) {
			double IG = h.calcInformationGain(rowCount, dt.dataset, i, attrCount - 1, entropyParent);
			// System.out.print(IG+" ");
			if (IG > maxIG) {
				maxIG = IG;
				attrToBeUsed = i;
			}
		}
		// System.out.println();
		// System.out.println(maxIG +" "+attrToBeUsed);
		int leftRowCount = 0, rightRowCount = 0;
		for (int i = 0; i < rowCount; i++) {
			if (dt.dataset[i][attrToBeUsed] == 1) {
				leftRowCount++;
			} else {
				rightRowCount++;
			}
		}
		dt.rowCount = rowCount;
		dt.leftTree = new Tree(++count);
		dt.rightTree = new Tree(++count);

		dt.leftTree.dataset = new int[leftRowCount][attrCount - 1];
		dt.rightTree.dataset = new int[rightRowCount][attrCount - 1];

		int p1 = 0, q1 = 0, p2 = 0, q2 = 0;
		for (int i = 0; i < rowCount; i++) {
			if (dt.dataset[i][attrToBeUsed] == 1) {
				for (int j = 0; j < attrCount; j++) {
					if (j == attrToBeUsed) {
						// q1++;
						continue;
					} else {
						dt.leftTree.dataset[p1][q1] = dt.dataset[i][j];
						q1++;
					}
				}
				p1++;
				q1 = 0;
			} else {
				for (int j = 0; j < attrCount; j++) {
					if (j == attrToBeUsed) {
						// q2++;
						continue;
					} else {
						dt.rightTree.dataset[p2][q2] = dt.dataset[i][j];
						q2++;
					}
				}
				p2++;
				q2 = 0;
			}
		}

		/*
		 * for(int i=0;i<leftRowCount;i++){ for(int j=0;j<attrCount-1;j++){
		 * System.out.print(dt.leftTree.dataset[i][j]+" "); }
		 * System.out.println(); }
		 * 
		 * for(int i=0;i<rightRowCount;i++){ for(int j=0;j<attrCount-1;j++){
		 * System.out.print(dt.rightTree.dataset[i][j]+" "); }
		 * System.out.println(); }
		 */
		String[] newArr = new String[attr.length - 1];
		// System.out.println("New Array: ");
		for (int i = 0, j = 0; i < attr.length; i++) {
			if (attrToBeUsed == i) {
				continue;
			} else {
				newArr[j] = attr[i];
				// System.out.print(newArr[j]+" ");
				j++;
			}
		}
		// System.out.println();
		dt.leftTree.setAttrUsed(attr[attrToBeUsed]);
		for (int i = attrMax; i > attrCount; i--)
			System.out.print("|  ");
		System.out.print(dt.leftTree.getAttrUsed() + " = 1 : ");
		dt.leftTree.rowCount = leftRowCount;
		recurseTree(dt.leftTree, leftRowCount, attrCount - 1,
				h.individualEntropyCalc(rowCount, 1, dt.dataset, attrToBeUsed, attrCount - 1), newArr);
		dt.rightTree.setAttrUsed(attr[attrToBeUsed]);
		for (int i = attrMax; i > attrCount; i--)
			System.out.print("|  ");
		System.out.print(dt.rightTree.getAttrUsed() + " = 0 : ");
		dt.rightTree.rowCount = rightRowCount;
		recurseTree(dt.rightTree, rightRowCount, attrCount - 1,
				h.individualEntropyCalc(rowCount, 0, dt.dataset, attrToBeUsed, attrCount - 1), newArr);

	}
}
