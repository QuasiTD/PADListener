package fr.neraud.padlistener.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neraud on 28/09/2014.
 */
public class BaseMonsterStatsModel implements Serializable {

	// Currently there are a maximum of five latent awakenings
	private static final int MAX_LATENTS = 5;

	private int idJp;
	private long exp;
	private int level;
	private int skillLevel;
	private int plusHp;
	private int plusAtk;
	private int plusRcv;
	private int awakenings;
	private int[] latentAwakenings = new int[MAX_LATENTS];

	public int getIdJp() {
		return idJp;
	}

	public void setIdJp(int idJp) {
		this.idJp = idJp;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getPlusHp() {
		return plusHp;
	}

	public void setPlusHp(int plusHp) {
		this.plusHp = plusHp;
	}

	public int getPlusAtk() {
		return plusAtk;
	}

	public void setPlusAtk(int plusAtk) {
		this.plusAtk = plusAtk;
	}

	public int getPlusRcv() {
		return plusRcv;
	}

	public void setPlusRcv(int plusRcv) {
		this.plusRcv = plusRcv;
	}

	public int getAwakenings() {
		return awakenings;
	}

	public void setAwakenings(int awakenings) {
		this.awakenings = awakenings;
	}

	public int[] getLatentAwakenings() {
		return latentAwakenings;
	}

	public void setLatentAwakenings(int[] latentAwakenings) {
		// Clear the old latent data
		for (int i = 0; i < MAX_LATENTS; ++i) {
			this.latentAwakenings[i] = 0;
		}

		// Copy the elements instead of assigning to ensure the length of this.latentAwakenings stays constant
		if (latentAwakenings != null) {
			System.arraycopy(latentAwakenings, 0, this.latentAwakenings, 0, Math.min(latentAwakenings.length, this.latentAwakenings.length));
		}
	}

	public void setLatentAwakeningsFromPackedInt(int packedLatentAwakenings) {
		// Each latent awakening is specified with five bits
		final int LATENT_WIDTH = 5;
		final int LATENT_MASK = (1 << LATENT_WIDTH) - 1;
		// The packed latents are appended by a four bit signature with a value of 5
		final int SIGNATURE_WIDTH = 4;
		final int SIGNATURE_MASK = (1 << SIGNATURE_WIDTH) - 1;
		final int SIGNATURE_VALUE = 5;

		// Verify signature before doing anything
		if ((packedLatentAwakenings & SIGNATURE_MASK) == SIGNATURE_VALUE) {
			// Shift out the signature
			packedLatentAwakenings = packedLatentAwakenings >> SIGNATURE_WIDTH;

			List<Integer> latents = new ArrayList<>(MAX_LATENTS);
			for (int i = 0; (i < MAX_LATENTS) && (packedLatentAwakenings != 0); ++i) {
				// Always push to the head of the list because the awakenings are stored in order with zero padding
				// eg. 1 awakening  = 0000A
				//     2 awakenings = 000AB
				latents.add(0, packedLatentAwakenings & LATENT_MASK);
				packedLatentAwakenings = packedLatentAwakenings >> LATENT_WIDTH;
			}

			// There's no built in way to convert from List<Integer> to int[]
			int i = 0;
			for (Integer e : latents) {
				this.latentAwakenings[i++] = e.intValue();
			}
		} else {
			// Fill with empty values
			for (int i = 0; i < MAX_LATENTS; ++i) {
				this.latentAwakenings[i] = 0;
			}
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(").append(idJp).append(") ");
		builder.append(skillLevel).append(" skill, ");
		builder.append("+").append(plusHp);
		builder.append(" +").append(plusAtk);
		builder.append(" +").append(plusRcv);
		builder.append(" ").append(awakenings);
		return builder.toString();
	}
}
