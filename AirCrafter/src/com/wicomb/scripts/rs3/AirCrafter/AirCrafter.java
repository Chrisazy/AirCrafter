package com.wicomb.scripts.rs3.AirCrafter;

import java.awt.Graphics;
import java.util.ArrayList;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.Skills;

import com.wicomb.scripts.rs3.AirCrafter.Tasks.BankTask;
import com.wicomb.scripts.rs3.AirCrafter.Tasks.CraftTask;
import com.wicomb.scripts.rs3.AirCrafter.Tasks.RandomTask;
import com.wicomb.scripts.rs3.AirCrafter.Tasks.RunTask;
import com.wicomb.scripts.rs3.framework.Task;

@Manifest(name = "Wicomb's AirCrafter", description = "Crafts air runes", properties = "topic=90210;client=6;")
public class AirCrafter extends
		PollingScript<org.powerbot.script.rt6.ClientContext> implements
		PaintListener {

	private int startLevel = 0;
	private int startExp = 0;
	private ArrayList<Task> tasks;
	private Task busyTask = null; // This is used in cases like walking where I
									// literally don't want anything to be done

	@Override
	public void start() {
		if (!ctx.game.loggedIn()) {
			System.out.println("Please start the script while logged in");
			ctx.controller.stop();
		}
		startLevel = ctx.skills.levels()[Skills.RUNECRAFTING];
		startExp = ctx.skills.experiences()[Skills.RUNECRAFTING];
		tasks = new ArrayList<Task>();
		tasks.add(new RandomTask(ctx));
		tasks.add(new RunTask(ctx));
		tasks.add(new BankTask(ctx));
		tasks.add(new CraftTask(ctx));
		System.out.println("Script started");
	}

	@Override
	public void poll() {
		if (busyTask != null && busyTask.busy == false) {
			busyTask = null;

		} else {

			for (Task t : tasks) {
				if (this.busyTask == null && t.busy)
					this.busyTask = t;

				if (t.activate()
						&& (this.busyTask == null || t.override || this.busyTask == t)) {
					t.execute();
				}
			}
		}
	}

	@Override
	public void repaint(Graphics arg0) {
		// TODO Auto-generated method stub

	}
}
