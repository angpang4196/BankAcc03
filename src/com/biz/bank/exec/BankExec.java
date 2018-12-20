package com.biz.bank.exec;

import com.biz.bank.service.BankService;

public class BankExec {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String balFile = "src/com/biz/bank/bankbalance.txt";

		BankService bs = new BankService(balFile);

		bs.readBalance();

		bs.bankMenu();

	}

}
