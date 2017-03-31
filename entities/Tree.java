package entities;

public class Tree {
	public int[][] dataset;
	public String attrUsed;
	public int classObject;
	public Tree leftTree;
	public Tree rightTree;
	public int rowCount;
	public int nodeNumber;
	
	public Tree(){
		leftTree = null;
		rightTree = null;
	}
	public Tree(int i){
		nodeNumber=i;
		leftTree = null;
		rightTree = null;
	}
	
	/**
	 * @return the dataset
	 */
	public int[][] getDataset() {
		return dataset;
	}
	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(int[][] dataset) {
		this.dataset = dataset;
	}
	/**
	 * @return the attrUsed
	 */
	public String getAttrUsed() {
		return attrUsed;
	}
	/**
	 * @param attrUsed the attrUsed to set
	 */
	public void setAttrUsed(String attrUsed) {
		this.attrUsed = attrUsed;
	}
	/**
	 * @return the classObject
	 */
	public int getClassObject() {
		return classObject;
	}
	/**
	 * @param classObject the classObject to set
	 */
	public void setClassObject(int classObject) {
		this.classObject = classObject;
	}
	/**
	 * @return the leftTree
	 */
	public Tree getLeftTree() {
		return leftTree;
	}
	/**
	 * @param leftTree the leftTree to set
	 */
	public void setLeftTree(Tree leftTree) {
		this.leftTree = leftTree;
	}
	/**
	 * @return the rightTree
	 */
	public Tree getRightTree() {
		return rightTree;
	}
	/**
	 * @param rightTree the rightTree to set
	 */
	public void setRightTree(Tree rightTree) {
		this.rightTree = rightTree;
	}

	/**
	 * @return the nodeNumber
	 */
	public int getNodeNumber() {
		return nodeNumber;
	}

	/**
	 * @param nodeNumber the nodeNumber to set
	 */
	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}
	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}
	/**
	 * @param rowCount the rowCount to set
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
}
