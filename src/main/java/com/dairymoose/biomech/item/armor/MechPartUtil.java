package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

public class MechPartUtil {
	public static List<ModelPart> getCorrespondingModelParts(PlayerModel playerModel, MechPart mechPart) {
		List<ModelPart> list = new ArrayList<>();
		if (mechPart != null) {
			switch (mechPart) {
			case Head:
				list.add(playerModel.head);
				break;
			case LeftArm:
				list.add(playerModel.leftArm);
				list.add(playerModel.leftSleeve);
				break;
			case RightArm:
				list.add(playerModel.rightArm);
				list.add(playerModel.rightSleeve);
				break;
			case Chest:
				list.add(playerModel.body);
				break;
			case Leggings:
				list.add(playerModel.leftLeg);
				list.add(playerModel.leftPants);
				list.add(playerModel.rightLeg);
				list.add(playerModel.rightPants);
				break;
			}
		}
		return list;
	}
}
