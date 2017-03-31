package controller;

public class Helper {
	
	public double makeDataForEntropyCalc(int rowcount, int className, int[][] dataSet, int attributeNum, int classCol){
		int[] dataClass = new int[rowcount];
		int p=0,t=0;
		for(int i = 0,j=0;i<rowcount;i++){
			if(dataSet[i][attributeNum]==className){
				dataClass[j] = dataSet[i][classCol];
				j++;
				p++;
				t++;
			}else{
				t++;
			}
		}
		return ((p/(double)t)*calcEntropy(dataClass));
	}
	public double individualEntropyCalc(int rowcount, int className, int[][] dataSet, int attributeNum, int classCol){
		int[] dataClass = new int[rowcount];
		for(int i = 0,j=0;i<rowcount;i++){
			if(dataSet[i][attributeNum]==className){
				dataClass[j] = dataSet[i][classCol];
				j++;
			}
		}
		return calcEntropy(dataClass);
	}
	public int positiveClasses(int rowcount, int className, int[][] dataSet, int attributeNum, int classCol){
		int p=0,t=0;
		for(int i = 0,j=0;i<rowcount;i++){
			if(dataSet[i][attributeNum]==className){
				p++;
			}
		}
		return p;
	}
	public double calcEntropy(int[] dataClass){
		
		int positiveCount = 0;
		int negativeCount = 0;
		for(int i = 0;i<dataClass.length;i++){
			
			if(dataClass[i]==1)
				positiveCount++;
			else
				negativeCount++;
		}
		
		double p=(positiveCount/(double)dataClass.length),q=(negativeCount/(double)dataClass.length);
		if(p==0){
			p=1;
			
		} else if(q==0){
			q=1;
		}
		
		
		return ((-p)*((Math.log(p))/Math.log(2)))+((-q)*((Math.log(q))/Math.log(2)));
	}
	
	public double calcInformationGain(int rowcount,int[][] dataSet, int attributeNum, int classCol,double parentEntropy){
		
		double totEntropy = makeDataForEntropyCalc(rowcount, 1,dataSet,attributeNum,classCol) + makeDataForEntropyCalc(rowcount, 0,dataSet, attributeNum,classCol);
		return parentEntropy - totEntropy;
	}
}
