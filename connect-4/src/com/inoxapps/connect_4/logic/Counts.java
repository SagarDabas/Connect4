package com.inoxapps.connect_4.logic;

public class Counts {
	private int temp1Count;
	private int temp2Count;
	private int temp3Count;
	private int oneCount;
	private int twoCount;
	private int threeCount;

	public void incrementsCounts(int check) {
		if (check == 2) {
			temp2Count++;
		} else if (check == 3) {
			temp3Count++;
		} else if (check == 1) {
			temp1Count++;
		}
	}

	public void clearTempCounts() {
		temp1Count = 0;
		temp2Count = 0;
		temp3Count = 0;
	}

	public void clearCounts() {
		oneCount = 0;
		twoCount = 0;
		threeCount = 0;
	}

	public void updateCounts() {
		oneCount += temp1Count;
		twoCount += temp2Count;
		threeCount += temp3Count;
	}

	public int getOneCount() {
		return oneCount;
	}

	public int getTwoCount() {
		return twoCount;
	}

	public int getThreeCount() {
		return threeCount;
	}
	
	
	

}
