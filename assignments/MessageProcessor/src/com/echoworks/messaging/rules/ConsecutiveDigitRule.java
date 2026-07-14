package com.echoworks.messaging.rules;

import com.echoworks.messaging.Message;

public class ConsecutiveDigitRule implements MessageRule{

	@Override
	public boolean check(Message msg) {
		String words [] = msg.getBody().split(" ");
		for(String word : words) {
			if(isConsecutive10Digits(word))
				return true;
		}
		return false;
	}

	private boolean isConsecutive10Digits(String word) {
		int cnt = 0;
		for(char ch : word.toCharArray()) {
			if(Character.isDigit(ch) && cnt < 10) {
				cnt++;
			}
			if(cnt == 10)
				break;
		}
		return cnt == 10 ;
	}
	
}
