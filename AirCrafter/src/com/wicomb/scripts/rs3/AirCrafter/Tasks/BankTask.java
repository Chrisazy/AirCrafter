package com.wicomb.scripts.rs3.AirCrafter.Tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import com.wicomb.scripts.rs3.framework.Constants;
import com.wicomb.scripts.rs3.framework.Helpers;
import com.wicomb.scripts.rs3.framework.Task;

public class BankTask extends Task {

	public BankTask(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "BankTask";
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Constants.RUNE_ESSENCE).count() == 0;
	}

	@Override
	public void execute() {
		if (!ctx.objects.select().id(Constants.PORTAL).within(10).isEmpty()) {
			System.out.println("Going to portal");
			GameObject portal = ctx.objects.poll();
			ctx.camera.turnTo(portal);
			if (portal.interact("Enter")) {
				Condition.wait(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						if (ctx.objects.select().id(Constants.PORTAL).isEmpty()) {
							return true;
						}
						return false;
					}
				});
			}
		} else {
			System.out.println("In bank");
			if (Helpers.distance(Constants.AIR_BANK_TILE, ctx) < 7) {
				// Then I need to do banking
				if (ctx.bank.open()) {
					Condition.wait(new Callable<Boolean>() {

						@Override
						public Boolean call() throws Exception {
							if (ctx.bank.opened())
								return true;
							return false;
						}
					});
				}
				if (ctx.bank.opened()) {
					if (ctx.backpack.select().id(Constants.RUNE_ESSENCE)
							.isEmpty()) {
						ctx.bank.depositInventory();
						if (ctx.bank.select().id(Constants.RUNE_ESSENCE)
								.isEmpty())
							ctx.controller.stop();
						ctx.bank.withdraw(Constants.RUNE_ESSENCE, 28);
						Condition.wait(new Callable<Boolean>() {

							@Override
							public Boolean call() throws Exception {
								return !ctx.backpack.select()
										.id(Constants.RUNE_ESSENCE).isEmpty();
							}
						});
					}
					ctx.bank.close();
				}
			} else {
				Helpers.stepAndWait(Constants.AIR_BANK_TILE,ctx);
			}
		}
	}
}
