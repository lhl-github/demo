package com.xljy;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class TTTtest {

	public static void main(String[] args) throws InterruptedException, IOException {

		// System.out.println(NumberUtil.add(66.66, 66.67));

		/*
		 * double div = NumberUtil.div(7, 3, 3, RoundingMode.HALF_UP);
		 * double div2 = NumberUtil.div(0, 1,2, RoundingMode.HALF_UP);
		 * double div3 = NumberUtil.div(1,3, 2, RoundingMode.HALF_UP);
		 * 
		 * System.out.println(div3);
		 * System.out.println(div3+"%");
		 * 
		 * System.out.println(div);
		 * System.out.println(div2);
		 */
		// BigDecimal div = NumberUtil.div("6200", "100",2);
		File file = new File("D:/图2.png");

		File file2 =new File("D:/temp_tupian_YS.jpg");
		Thumbnails.of(file).scale(1f)// 压缩后比例不变
		.outputQuality(0.4f)// 压缩后图片质量
		.toFile(file2);
		
	}

}
