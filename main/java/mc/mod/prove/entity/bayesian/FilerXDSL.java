package mc.mod.prove.entity.bayesian;

public class FilerXDSL {
	static final String MEJO = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
			"<smile version=\"1.0\" id=\"Network1\" numsamples=\"1000\" discsamples=\"10000\">" +
			"	<nodes>" +
			"		<cpt id=\"State_t\">" +
			"			<state id=\"Hunt\" />" +
			"			<state id=\"Suspect\" />" +
			"			<state id=\"Flee\" />" +
			"			<state id=\"LookAround\" />" +
			"			<state id=\"Trick\" />" +
			"			<probabilities>0.2 0.2 0.2 0.2 0.2</probabilities>" +
			"		</cpt>" +
			"		<cpt id=\"State_t1\">" +
			"			<state id=\"Hunt\" />" +
			"			<state id=\"Suspect\" />" +
			"			<state id=\"Flee\" />" +
			"			<state id=\"LookAround\" />" +
			"			<state id=\"Trick\" />" +
			"			<parents>State_t</parents>" +
			"			<probabilities>0.3332666599982222 0.3332666566645556 0.0001000133363333 0.3332666566645556 0.00010001333633336 0.33326666 0.33326666 0.0001 0.33326666 0.0001 0.0001 0.0001 0.49985 0.49985 0.0001 0.1975 0.1975 0.1975 0.21 0.1975 0.0001 0.3249 0.0001 0.3249 0.35</probabilities>" +
			"		</cpt>" +
			"		<cpt id=\"Player_In_Sight\">" +
			"			<state id=\"Close\" />" +
			"			<state id=\"Far\" />" +
			"			<state id=\"None\" />" +
			"			<parents>State_t1</parents>" +
			"			<probabilities>0.5 0.5 0 0 0 1 0.4666666666666667 0.3333333333333333 0.2 0 0 1 0 0 1</probabilities>" +
			"		</cpt>" +
			"		<cpt id=\"Step_Sound\">" +
			"			<state id=\"Close\" />" +
			"			<state id=\"Far\" />" +
			"			<state id=\"None\" />" +
			"			<parents>State_t1</parents>" +
			"			<probabilities>0.3333333333333334 0.3333333333333334 0.3333333333333333 0.45 0.45 0.1 0.7 0.299 0.001 0.001 0.001 0.998 0.001 0.001 0.998</probabilities>" +
			"		</cpt>" +
			"		<cpt id=\"Timer\">" +
			"			<state id=\"RunningOut\" />" +
			"			<state id=\"Normal\" />" +
			"			<parents>State_t1</parents>" +
			"			<probabilities>1e-05 0.99999 1e-06 0.999999 0.9999999000000001 1e-07 0.0001 0.9999 0.0001 0.9999</probabilities>" +
			"		</cpt>" +
			"		<cpt id=\"Lighting_Change\">" +
			"			<state id=\"Close\" />" +
			"			<state id=\"Far\" />" +
			"			<state id=\"None\" />" +
			"			<parents>State_t1</parents>" +
			"			<probabilities>0.3333333333333334 0.3333333333333334 0.3333333333333333 0.45 0.45 0.1 0.7 0.299 0.001 0.001 0.001 0.998 0.001 0.001 0.998</probabilities>" +
			"		</cpt>" +
			"		<cpt id=\"Sound_Block\">" +
			"			<state id=\"Close\" />" +
			"			<state id=\"Far\" />" +
			"			<state id=\"None\" />" +
			"			<parents>State_t1</parents>" +
			"			<probabilities>0.3333333333333334 0.3333333333333334 0.3333333333333333 0.45 0.45 0.1 0.7 0.299 0.001 0.001 0.001 0.998 0.001 0.001 0.998</probabilities>" +
			"		</cpt>" +
			"		<cpt id=\"Player_Tricking\">" +
			"			<state id=\"Likely\" />" +
			"			<state id=\"Unlikely\" />" +
			"			<state id=\"Uncertain\" />" +
			"			<parents>State_t1</parents>" +
			"			<probabilities>0.3333333333333334 0.3333333333333334 0.3333333333333333 1e-09 0.9999899990000001 1e-05 0.5 0.001 0.499 0.5 0.001 0.499 0.5 0.001 0.499</probabilities>" +
			"		</cpt>" +
			"	</nodes>" +
			"	<extensions>" +
			"		<genie version=\"1.0\" app=\"GeNIe 2.1.380.0\" name=\"Network1\" faultnameformat=\"nodestate\">" +
			"			<node id=\"State_t1\">" +
			"				<name>State t+1</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>433 65 481 95</position>" +
			"				<barchart active=\"true\" width=\"174\" height=\"108\" />" +
			"			</node>" +
			"			<node id=\"Player_In_Sight\">" +
			"				<name>Player In Sight</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>86 370 157 414</position>" +
			"				<barchart active=\"true\" width=\"120\" height=\"72\" />" +
			"			</node>" +
			"			<node id=\"Step_Sound\">" +
			"				<name>Step Sound</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>250 372 319 415</position>" +
			"				<barchart active=\"true\" width=\"128\" height=\"72\" />" +
			"				<defcomment row=\"0\" col=\"0\">Il senso ï¿½ determinare quanto lo stato successivo dipende dall&apos;aver sentito un suono ad una certa distanza\\n</defcomment>" +
			"			</node>" +
			"			<node id=\"Timer\">" +
			"				<name>Timer</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>67 216 115 246</position>" +
			"				<barchart active=\"true\" width=\"118\" height=\"54\" />" +
			"			</node>" +
			"			<node id=\"Lighting_Change\">" +
			"				<name>Lighting Change</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>412 368 500 423</position>" +
			"				<barchart active=\"true\" width=\"139\" height=\"72\" />" +
			"			</node>" +
			"			<node id=\"Sound_Block\">" +
			"				<name>Sound Block</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>627 372 700 417</position>" +
			"				<barchart active=\"true\" width=\"128\" height=\"72\" />" +
			"			</node>" +
			"			<node id=\"Player_Tricking\">" +
			"				<name>Player Tricking</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>662 177 744 228</position>" +
			"				<barchart active=\"true\" width=\"128\" height=\"64\" />" +
			"				<defcomment row=\"0\" col=\"1\">If it&apos;s likely or unlikely, it considers the player is not in sight, probably will add a rule to that in the code</defcomment>" +
			"			</node>" +
			"			<node id=\"State_t\">" +
			"				<name>State t</name>" +
			"				<interior color=\"e5f6f7\" />" +
			"				<outline color=\"000080\" />" +
			"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
			"				<position>132 64 180 94</position>" +
			"				<barchart active=\"true\" width=\"174\" height=\"108\" />" +
			"			</node>" +
			"		</genie>" +
			"	</extensions>" +
			"</smile>";
}