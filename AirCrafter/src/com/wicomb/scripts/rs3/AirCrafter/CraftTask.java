package com.wicomb.scripts.rs3.AirCrafter;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import com.wicomb.scripts.rs3.framework.Constants;
import com.wicomb.scripts.rs3.framework.Helpers;
import com.wicomb.scripts.rs3.framework.Task;

public class CraftTask extends Task {

	public CraftTask(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Constants.RUNE_ESSENCE).count() > 0;
	}

	@Override
	public void execute() {
		/* Condition.wait(new Callable<Boolean>() {

						@Override
						public Boolean call() throws Exception {
							if (ctx.bank.opened())
								return true;
							return false;
						}
					});
		*/
		if(!ctx.objects.select().id(Constants.AIR_ALTAR).within(10).isEmpty()) {
			System.out.println("In Altar");
			GameObject altar = ctx.objects.poll();
			if(Helpers.distance(altar,ctx) > 3) {
				ctx.movement.step(altar);
			} else if (altar.inViewport()) {
				altar.interact("Craft-rune");
				Condition.wait(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						return (ctx.backpack.select().id(Constants.RUNE_ESSENCE).isEmpty());
					}
				});
			} else {
				ctx.camera.turnTo(altar);
				return;
			}
		}
		if (Helpers.distance(Constants.AIR_ALTAR_TILE, ctx) > 5) {
			System.out.println("Walking to Altar");
			Helpers.stepAndWait(Constants.AIR_ALTAR_TILE, ctx);
		} else {
			GameObject altar = ctx.objects.select().id(Constants.AIR_ENTRANCE)
					.poll();
			if (altar.inViewport()) {
				altar.interact("Enter");
				Condition.wait(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						return (ctx.objects.select().id(Constants.AIR_ENTRANCE).isEmpty());
					}
				});
			} else {
				ctx.camera.turnTo(altar);
				return;
			}
		}

	}
}
