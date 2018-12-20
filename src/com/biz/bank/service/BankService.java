package com.biz.bank.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.biz.bank.vo.BankVO;

public class BankService {

	List<BankVO> bankList;
	String balFile;
	Scanner scan;

	public BankService(String balFile) {
		bankList = new ArrayList();
		this.balFile = balFile;
		scan = new Scanner(System.in);
	}

	public void readBalance() {
		FileReader fr;
		BufferedReader buffer;

		try {
			fr = new FileReader(balFile);
			buffer = new BufferedReader(fr);

			while (true) {
				String reader = buffer.readLine();
				if (reader == null)
					break;
				String[] strSp = reader.split(":");

				BankVO vo = new BankVO();

				vo.setStrID(strSp[0]);
				vo.setIntBalance(Integer.valueOf(strSp[1]));
				vo.setStrLastDate(strSp[2]);

				bankList.add(vo);

			}
			buffer.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // readBalance method end

	public void bankMenu() {

		System.out.println("==================================================");
		System.out.println("1.입금  2.출금  3.계좌조회  0.종료");
		System.out.println("--------------------------------------------------");
		System.out.print("하실 용무를 선택하여 숫자입력 >>>");

		String strMenu = scan.nextLine();
		int intMenu = Integer.valueOf(strMenu);

		if (intMenu == 0) {
			System.out.println("프로그램을 종료합니다.");
		}
		if (intMenu == 1) {
			this.bankInput();
		}
		if (intMenu == 2) {
			this.bankOutput();
		}
		if (intMenu == 3) {
			System.out.println("계좌조회");
		}
	} // bankMenu method end

	public void bankInput() {
		System.out.println("--------------------------------------------------");
		System.out.print("입금 계좌번호 입력 >>>");
		String strID = scan.nextLine();
		System.out.println("--------------------------------------------------");
		BankVO vo = bankFindId(strID);

		if (vo == null) {
			System.out.println("게좌번호 오류");
			return;
		}
		// 계좌번호가 정상이고, vo에는 해당 계좌번호의 정보가 담겨있다.

		System.out.print("입금액을 입력 >>>");
		String strIO = scan.nextLine();
		int intIO = Integer.valueOf(strIO);

		// 입금일 경우 vo.strIO 에 "입금" 문자열 저장
		// vo.intIOCash에 입금액을 저장
		// vo.intBalance에 +입금액을 저장

		vo.setStrIO("입금");
		vo.setIntIOCash(intIO);
		vo.setIntBalance(vo.getIntBalance() + intIO);

		// old java 코드로 현재 날짜 가져오기
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		String strDate = sm.format(curDate);

		// new java(1.8) 코드로 현재 날짜 가져오기
		LocalDate ld = LocalDate.now();
		strDate = ld.toString();

		vo.setStrLastDate(strDate);

		this.bankIOWrite(vo);

		System.out.println("입금이 완료되었습니다.");

	} // bankInput method end

	public void bankOutput() {
		System.out.println("--------------------------------------------------");
		System.out.print("출금 계좌번호 입력 >>>");
		String strID = scan.nextLine();
		System.out.println("--------------------------------------------------");
		BankVO vo = bankFindId(strID);

		if (vo == null) {
			System.out.println("게좌번호 오류");
			return;
		}
		System.out.print("출금액을 입력 >>>");
		String strIO = scan.nextLine();
		int intIO = Integer.valueOf(strIO);

		if (vo.getIntBalance() < intIO) {
			System.out.println("잔액이 부족합니다.");
			return;
		}

		vo.setStrIO("출금");
		vo.setIntIOCash(intIO);
		vo.setIntBalance(vo.getIntBalance() - intIO);

		LocalDate ld = LocalDate.now();
		String strDate = ld.toString();

		vo.setStrLastDate(strDate);

		this.bankIOWrite(vo);

		System.out.println("출금이 완료되었습니다.");
	}

	// 계좌번호를 매개변수로 받아서 bankList에서 계좌를 조회하고
	// bankList에 계좌가 있으면 찾은 BankVO(vo)를 return하고
	// 없으면 null 값을 return하도록 한다.
	public BankVO bankFindId(String strID) {
		for (BankVO vo : bankList) {
			if (vo.getStrID().equals(strID)) {
				return vo;
			}
		}
		return null;
	} // bankFindId method end

	public void bankIOWrite(BankVO vo) {
		FileWriter fw;
		PrintWriter pw;

		try {

			String ResultF = "src/com/biz/bank/iolist/" + vo.getStrID() + ".txt";
			fw = new FileWriter(ResultF, true);
			pw = new PrintWriter(fw);

			pw.print(vo.getStrLastDate() + ":" + vo.getStrID() + ":" + vo.getStrIO());

			if (vo.getStrIO().equals("입금")) {
				pw.println(":" + vo.getIntIOCash() + ":" + "0" + ":" + vo.getIntBalance());

			}

			if (vo.getStrIO().equals("출금")) {
				pw.println(":" + "0" + ":" + vo.getIntIOCash() + ":" + vo.getIntBalance());

			}

			pw.close();
			fw.close();

			System.out.println("거래내역 저장완료");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
