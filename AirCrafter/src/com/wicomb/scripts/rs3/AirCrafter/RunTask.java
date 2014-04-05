package com.wicomb.scripts.rs3.AirCrafter;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

import com.wicomb.scripts.rs3.framework.Helpers;
import com.wicomb.scripts.rs3.framework.Task;

public class RunTask extends Task {

	public RunTask(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		return ctx.movement.energyLevel() > 35 && !ctx.movement.running();
	}

	@Override
	public void execute() {
		if(Helpers.chance(1)) {
			ctx.movement.running(true);
			Condition.wait(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return ctx.movement.running();
				}
			});
		}
	}

}
