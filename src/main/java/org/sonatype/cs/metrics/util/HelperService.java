package org.sonatype.cs.metrics.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.springframework.stereotype.Service;

@Service
public class HelperService {

	public int[] getPointsSumAndAverage(List<DbRow> dataList) {

		int countLabels = 0;
		int sumData = 0;

		for (DbRow dp : dataList) {
			int count = (int) dp.getPointA();

			if (count > 0) {
				sumData += count;
				countLabels++;
			}
		}

		int avg = sumData / countLabels;

		int[] values = new int[] { sumData, avg };
		return values;
	}
	
	
	public Object getPointsAverage(List<Float> points) {
		int countPoints = 0;

		float sumData = 0;

		for (float dp : points) {

			if (dp > 0) {
				sumData += dp;
				countPoints++;
			}
		}

		float a = sumData / countPoints;
		
		if (Float.isNaN(a)) {
			a = 0;
		}
		
		return a;
	}
	
	public Map<String, Object> dataMap(String key, List<DbRowStr> data) {
		
		Map<String, Object> map = new HashMap<>();
		
		if (data.size() > 0){
			map.put(key + "Data", data);
			map.put(key + "Number", data.size());
			map.put(key, true);
		}
		else {
			map.put(key + "Number", 0);
			map.put(key, false);
		}
		
		return map;
	}
	
	public String calculateDivision(float a, float b){
	    String result;

	    if (a > 0 && b > 0){
	      double increase = (double) a / b;
	      result = String.format("%.2f", increase); 
	    }
	    else {
	      result = "0";
	    }
	    
	    return result;

	}
}
