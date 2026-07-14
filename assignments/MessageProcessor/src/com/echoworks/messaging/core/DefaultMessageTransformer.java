package com.echoworks.messaging.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echoworks.messaging.Message;
import com.echoworks.messaging.MessageProcessorException;
import com.echoworks.messaging.action.Action;
import com.echoworks.messaging.action.ReplaceCharAction;
import com.echoworks.messaging.action.ReverseWordAction;
import com.echoworks.messaging.rules.ConsecutiveDigitRule;
import com.echoworks.messaging.rules.DomainNameRule;
import com.echoworks.messaging.rules.MessageRule;
import com.echoworks.messaging.rules.SecureTxtRule;

public class DefaultMessageTransformer implements MessageTransformer{

	Message message ;
	
	Map<MessageRule,List<Action>> rulesToActionMap ;
	
	
	public DefaultMessageTransformer() {
		initDefaultRules();
	}
	
	public DefaultMessageTransformer(Message msg) {
		this.message  = msg;
		initDefaultRules();
	}

	@Override
	public void process() {
		if(message == null)
			throw new MessageProcessorException("Message not initialized null" );
		for(Map.Entry<MessageRule, List<Action>> entry : rulesToActionMap.entrySet()) {
			if(entry.getKey().check(message)) {
				for(Action action : entry.getValue()) {
					action.apply(message);
				}
			}
				
		}		
	}
	
	@Override
	public void process(Message message) {
		this.message = message;
		for(Map.Entry<MessageRule, List<Action>> entry : rulesToActionMap.entrySet()) {
			if(entry.getKey().check(message)) {
				for(Action action : entry.getValue()) {
					action.apply(message);
				}
			}
				
		}		
	}
	
	private void initDefaultRules() {
		rulesToActionMap = new HashMap<>();
		rulesToActionMap.put(new SecureTxtRule(),Arrays.asList(new ReverseWordAction()) );
		rulesToActionMap.put(new ConsecutiveDigitRule(),Arrays.asList(new ReplaceCharAction(),new ReverseWordAction()));
		rulesToActionMap.put(new DomainNameRule(),Arrays.asList(new ReplaceCharAction()));
	}
	
	public Map<MessageRule,List<Action>> getRulesToActionMap() {
		return rulesToActionMap;
	}

	public void setRulesToActionMap(Map<MessageRule,List<Action>> rulesToActionMap) {
		this.rulesToActionMap = rulesToActionMap;
	}
	
	public void addRuleActionMapping(MessageRule rule,List<Action> action) {
		if(this.rulesToActionMap != null)
			this.rulesToActionMap.put(rule, action);
	}
	
}
