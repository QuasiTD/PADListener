package fr.neraud.padlistener.helper;

import fr.neraud.padlistener.model.MonsterModel;

/**
 * Helper to determine if monsters are equals
 *
 * @author Neraud
 */
public class MonsterComparatorHelper {

	private static boolean compareCardId(MonsterModel a, MonsterModel b) {
		if (a.getCardId() == null) {
			return b.getCardId() == null;
		} else {
			return a.getCardId().equals(b.getCardId());
		}
	}

	private static boolean compareLatentAwakenings(MonsterModel a, MonsterModel b) {
		final int[] aLatents = a.getLatentAwakenings();
		final int[] bLatents = b.getLatentAwakenings();

		boolean bIsSame = aLatents.length == bLatents.length;
		for (int i = 0; bIsSame && (i < aLatents.length); ++i) {
			bIsSame &= aLatents[i] == bLatents[i];
		}

		return bIsSame;
	}

	public static boolean areMonstersEqual(MonsterModel a, MonsterModel b) {
		return a.getIdJp() == b.getIdJp() &&
				compareCardId(a, b) &&
				a.getExp() == b.getExp() &&
				a.getSkillLevel() == b.getSkillLevel() &&
				a.getPlusHp() == b.getPlusHp() &&
				a.getPlusAtk() == b.getPlusAtk() &&
				a.getPlusRcv() == b.getPlusRcv() &&
				a.getAwakenings() == b.getAwakenings() &&
				compareLatentAwakenings(a, b);
	}

}
