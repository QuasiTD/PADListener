
package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedMonsterFullInfoModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;

/**
 * Helper for the PlayerMonsterProvider
 * 
 * @author Neraud
 */
public class CapturedPlayerMonsterHelper extends BaseProviderHelper {

	/**
	 * @param cursor the query result
	 * @return the MonsterCardModel
	 */
	public static CapturedMonsterCardModel cursorToModel(Cursor cursor) {
		final CapturedMonsterCardModel model = new CapturedMonsterCardModel();

		model.setId(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID));
		model.setExp(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.EXP));
		model.setLevel(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.LEVEL));
		model.setSkillLevel(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.SKILL_LEVEL));
		model.setPlusHp(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.PLUS_HP));
		model.setPlusAtk(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.PLUS_ATK));
		model.setPlusRcv(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.PLUS_RCV));
		model.setAwakenings(getInt(cursor, CapturedPlayerMonsterDescriptor.Fields.AWAKENINGS));

		return model;
	}

	/**
	 * @param cursor the query result
	 * @return the CapturedMonsterFullInfoModel
	 */
	public static CapturedMonsterFullInfoModel cursorWithInfoToModel(Cursor cursor) {
		final CapturedMonsterCardModel capturedMonster = cursorToModel(cursor);
		final MonsterInfoModel monsterInfo = MonsterInfoHelper.cursorToModel(cursor);

		final CapturedMonsterFullInfoModel model = new CapturedMonsterFullInfoModel();
		model.setCapturedMonster(capturedMonster);
		model.setMonsterInfo(monsterInfo);

		return model;
	}

	/**
	 * @param status the MonsterCardModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(CapturedMonsterCardModel model) {
		final ContentValues values = new ContentValues();

		putValue(values, CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID, model.getId());
		putValue(values, CapturedPlayerMonsterDescriptor.Fields.EXP, model.getExp());
		putValue(values, CapturedPlayerMonsterDescriptor.Fields.LEVEL, model.getLevel());
		putValue(values, CapturedPlayerMonsterDescriptor.Fields.SKILL_LEVEL, model.getSkillLevel());
		putValue(values, CapturedPlayerMonsterDescriptor.Fields.PLUS_HP, model.getPlusHp());
		putValue(values, CapturedPlayerMonsterDescriptor.Fields.PLUS_ATK, model.getPlusAtk());
		putValue(values, CapturedPlayerMonsterDescriptor.Fields.PLUS_RCV, model.getPlusRcv());
		putValue(values, CapturedPlayerMonsterDescriptor.Fields.AWAKENINGS, model.getAwakenings());

		return values;
	}
}
