package com.wicomb.scripts.rs3.framework;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

public class Helpers {
	public static boolean chance(double c) {
		int upper = (int)(100/c);
		
		return Random.nextInt(1, upper) == 1;
	}
	
	public static boolean isFull(ClientContext ctx) {
		return ctx.backpack.select().count() == 28;
	}
	public static int distance(Locatable l,ClientContext ctx) {
		return (int) ctx.players.local().tile().distanceTo(l);
	}
	public static void stepAndWait(Locatable l,final ClientContext ctx)  {
		ctx.movement.step(l);
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				if (ctx.players.local().animation() == -1)
					return true;
				if (distance(ctx.movement.destination(),ctx) < 8)
					return true;
				return false;
			}
		});
	}
}
