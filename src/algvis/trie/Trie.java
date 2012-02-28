package algvis.trie;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;

import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;

public class Trie extends DataStructure {
	public static String adtName = "triea";
	public static String dsName = "triei";

	private TrieNode root = null;
	private TrieNode v = null;

	Vector<String> vsk = new Vector<String>();
	Vector<String> ven = new Vector<String>();

	public TrieHelpWord hw = null;

	public Trie(VisPanel M) {
		super(M);
		clear();
		vsk = initRandomSkText();
		ven = initRandomEnText();
	}

	@Override
	public String getName() {
		return "triei";
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {
		//
	}

	@Override
	public void clear() {
		root = new TrieNode(this);
		root.reposition();
		v = null;
	}

	public TrieNode getV() {
		return v;
	}

	public TrieNode setV(TrieNode v) {
		this.v = v;
		return v;
	}

	public TrieNode getRoot() {
		return root;
	}

	public void setRoot(TrieNode root) {
		this.root = root;
	}

	@Override
	public void draw(View V) {
		TrieNode v = getRoot();
		if (v != null) {
			v.moveTree();
			v.drawTree(V);
		}
		v = getV();
		if (v != null) {
			v.move();
			v.drawTrieCH(V);
		}
		if (hw != null) {
			hw.draw(V);
		}
	}

	@Override
	public void random(int n) {
		Random g = new Random(System.currentTimeMillis());
		boolean p = M.pause;
		M.pause = false;
		int r = g.nextInt(vsk.size());
		insert(vsk.elementAt(r));
		M.pause = p;
	}

	public void insert(String s) {
		start(new TrieInsert(this, s));
	}

	public void find(String s) {
		start(new TrieFind(this, s));
	}

	public void delete(String s) {
		start(new TrieDelete(this, s));
	}

	public void reposition() {
		getRoot().reposition();
	}

	public Vector<String> convertToVS(String text) {
		LinkedList<String> args = new LinkedList<String>(Arrays.asList(text
				.replaceAll("'", " ").split("(\\s|,)+")));
		int noa = args.size();
		for (int i = 0; i < noa; i++) {
			String s = args.poll();
			if (s.compareTo("_") != 0) {
				Pattern p = Pattern
						.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");
				s = Normalizer.normalize(s, Normalizer.Form.NFD);
				s = p.matcher(s).replaceAll("");
				s = s.toUpperCase(Locale.ENGLISH);
				s = s.replaceAll("[^_A-Z]", "");
			} else {
				s = "\u025B"; // ɛ = 0x025B
			}

			if (s.compareTo("") != 0) {
				args.addLast(s + "$");
			}
		}
		if (args.size() == 0) {
			args.add("$");
		}
		return new Vector<String>(args);
	}

	public void clearExtraColor() {
		TrieNode r = getRoot();
		if (r != null) {
			getRoot().clearExtraColor();
		}
	}

	private Vector<String> initRandomSkText() {
		String result = "Pokoj vládne v tomto kraji, "
				+ "mesto stráži cestu obchodnú, "
				+ "výhľad pekný -- nielen v Raji, "
				+ "na rodnú našu Tatru nemennú. "
				+ "Slovákovi dobre je, na poli sa darí, "
				+ "Uhor dane vyzbiera, no on bohatstva má dosť, "
				+ "osud mu však nepraje a snahu všetku marí, "
				+ "veľký, mocný Tatár, chystá zlámať mu kosť! " + " "
				+ "Na rýchlom koni mu to cvála, "
				+ "rezko je tu, takmer hneď. "
				+ "Slovák skrytý, v hore spáva, " + "bije Tata ako meď. "
				+ "A keďže Slovák nebol žiadne psisko, "
				+ "na vďaku Bohu, tu, postavil Kláštorisko. ";
		result += "Krásne skaly, divé rieky, uprostred lúka je, "
				+ "kostol tu trčí ako pocta všemocnému Bohu, "
				+ "Slovák má radosť, a len čo ošetrí si nohu, "
				+ "ďalej na poliach v pokoji usilovne pracuje. " + ""
				+ "Do hôr volá mníchov, ľudí s dobrým poslaním, "
				+ "oni azkéti sú, jedia ryby, chlieb a hrach, "
				+ "len raz za týždeň vidia za bránami mach, "
				+ "avšak najviac prekvapia Ťa mlčaním. " + ""
				+ "Ora et Labora -- modli sa a pracuj, dávne heslo platilo, "
				+ "písanie a modlenie sa napĺňalo život ich. "
				+ "Jeden hábit, v zime ovčia koža -- to vtedy sa nosilo, "
				+ "dnes sa trasiem -- nechcem byť jedným z nich. "
				+ "Veľmi sa však zaujímam, ako pri tom mlčaní, "
				+ "viedli život, mali slovo pri spoločnom sedení. ";
		result += "Zleteli orly z Tatry, tiahnu na podolia, "
				+ "ponad vysoké hory, ponad rovné polia; "
				+ "preleteli cez Dunaj, cez tú šíru vodu, "
				+ "sadli tam za pomedzím slovenského rodu. " + ""
				+ "Duní Dunaj a luna za lunou sa valí: "
				+ "nad ním svieti pevný hrad na vysokom bralí. "
				+ "Pod tým hradom Riman—cár zastal si táborom: "
				+ "belia sa rady šiatrov ďalekým priestorom. "
				+ "Pokraj táboru sedí cár na zlatom stolci; "
				+ "okol neho cárska stráž, tuhí to paholci; "
				+ "a pred cárom družina neveliká stojí: "
				+ "sú to cudzí víťazi, každý v jasnej zbroji. "
				+ "Pobelavé kaderie šije im obtáča, "
				+ "modré ich oči bystro v okolo si páča. "
				+ "Rastom sú ako jedle, pevní ako skala, "
				+ "zdalo by sa ti, že ich jedna mater mala. "
				+ "Krásna zem — jej končiny valný Dunaj vlaží, "
				+ "a Tatra skalnou hradbou okol nej sa väží: "
				+ "Tá zem, tie pyšné hory, tie žírne moravy: "
				+ "to vlasť ich, to kolíska dávna synov slávy. "
				+ "Slovenský rod ich poslal, zo slávneho snemu, "
				+ "aby išli s pozdravom k cárovi rímskemu. "
				+ "Oni čelom nebijú, do nôh nepadajú: "
				+ "taká otroč neznáma slovenskému kraju, "
				+ "lež božie dary nesú, chlieb a soľ, cárovi "
				+ "a smelými sa jemu primlúvajú slovy: "
				+ "Národ slovenský, kňazstvo i staršina naša, "
				+ "kroz nás ti, slávny cáre! svoj pozdrav prináša. "
				+ "Zem tá, na ktorú kročiť mieni tvoja noha, "
				+ "to je zem naša, daná Slovänom od Boha. "
				+ "Pozri: tu jej končiny valný Dunaj vlaží, "
				+ "tam Tatra skalnou hradbou okol nej sa väží. "
				+ "A zem to požehnaná! Chvála Bohu z neba, "
				+ "máme pri vernej práci voždy svoj kus chleba. "
				+ "Zvyk náš je nie napadať cudzie vlasti zbojom: "
				+ "Slovän na svojom seje, i žne len na svojom, "
				+ "cudzie nežiada. Ale keď na naše dvere "
				+ "zaklope ruka cudzia v úprimnej dôvere: "
				+ "kto je, ten je; či je on zblíza, či zďaleka: "
				+ "Vo dne, v noci na stole dar boží ho čaká. "
				+ "Pravda, bohy vydaná, káže nám Slovänom: "
				+ "pána mať je neprávosť a väčšia byť pánom. "
				+ "A človek nad človeka u nás nemá práva: "
				+ "sväté naše heslo je: Sloboda a sláva! — "
				+ "Neraz krásnu vlasť našu vrah napadol divý: "
				+ "na púšť obrátili sa bujné naše nivy; "
				+ "mestá ľahli popolom: a ľud náš úbohý, "
				+ "bitý biedami, cudzím dostal sa pod nohy. "
				+ "Bláhal už víťaz pyšný, že si bude pásti "
				+ "vôľu svoju naveky po slovänskej vlasti, "
				+ "a žiť z našich mozoľov: ale bláhal darmo! "
				+ "Dal nám Boh zas dobrý deň, zlomili sme jarmo. "
				+ "A tí, krutým železom čo nad nami vládli, "
				+ "kdeže sú? — My stojíme; ale oni padli. — "
				+ "Lebo — veky to svedčia — vo knihách osudu "
				+ "tak stojí napísané o slovänskom ľudu: "
				+ "Zem, ktorú v údel dali Slovänom nebesá, "
				+ "tá zem hrobom každému vrahovi stane sa. — "
				+ "Nuž, povedzže nám, cáre! mocná ruka tvoja "
				+ "čože nám nesie: či meč, či vetvu pokoja? "
				+ "S mečom ak ideš: cáre! meče máme i my, "
				+ "a poznáš, že narábať dobre vieme s nimi; "
				+ "ak s pokojom: pozdrav ťa pán neba i zeme, "
				+ "lepšie, ako ťa my tu pozdraviť umieme. — "
				+ "Tieto dary božie sú priazne našej znaky; "
				+ "z ďaky ti ich dávame: ber ich aj ty z ďaky.“ "
				+ "Nevzal cár božie dary, z jeho mračnej tvári "
				+ "urazená sa pýcha ľútnym hnevom žiari. "
				+ "A zo stolca zlatého takovým sa heslom "
				+ "ozvali ústa jeho ku slovänským poslom: "
				+ "Mocný pán, ktorému boh celú zem podnožil "
				+ "a osudy národov v ruku jeho vložil: "
				+ "ten pán velí: Slovänia! pozrite po svete: "
				+ "medzi národy jeho či jeden nájdete, "
				+ "ktorý by putá minul abo nezahynul, "
				+ "akže oproti Rímu prápor svoj rozvinul. "
				+ "Skloníte šije i vy. — Tie krásne roviny, "
				+ "túto zem vašich dedov dostane ľud iný. "
				+ "A spurné rody vaše pôjdu Rímu slúžiť, "
				+ "strážiť nám naše stáda, polia naše plúžiť. "
				+ "A junač vašu k mojim junákom pripojím, "
				+ "a z nej krajinám rímskym obranu pristrojím. "
				+ "A kto sa proti mojim rozkazom postaví, "
				+ "beda mu! ten sám sebe záhubu pripraví. "
				+ "Vedzte, že som pán Rímu, a Rím je pán svetu: "
				+ "To moja cárska vôľa; to vám na odvetu.“ " + ""
				+ "Hriema pyšný cár, hriema zo stolca zlatého, "
				+ "lenže Slovän nejde sa ľakať pýchy jeho. "
				+ "Hoj, rozovrela tá krv slovänská divoko, "
				+ "a junák ti cárovi pozrel okom v oko; "
				+ "a z oka ti mu božia zablysla sa strela, "
				+ "ruka sa napružila a na zbroj udrela; "
				+ "a jedným veľkým citom srdcia im zahrali; "
				+ "a jeden strašný ohlas ústa im vydali: "
				+ "„Mor ho!“ kríkla družina slovänská odrazu "
				+ "a meč zasvietil v pästi každému víťazu: "
				+ "„Mor ho!“ kríkla a razom na cára sa metá: "
				+ "To ti na rímsku pýchu slovänská odveta. "
				+ "No, dokáž teraz, či máš toľko sily v meči, "
				+ "koľko pýchy vo tvojej, cáre, bolo reči. — "
				+ "Ale ten nie! — zbroje sa bojí podlá duša: "
				+ "a tu ti o slobodu dobrý ľud pokúša. — "
				+ "Skočil medzi stráž svoju cár bledý od strachu: "
				+ "a zlatý jeho stolec už sa váľa v prachu; "
				+ "a mečom za ním Slovän cestu si preráža "
				+ "a junák za junákom padá cárska stráža. " + ""
				+ "Zasurmili surmity, volajú do zbroje: "
				+ "povstal tábor, do šíkov zvíjajú sa voje; "
				+ "a voj za vojom divým útokom ta letí, "
				+ "kde boj na cára bijú tie slovänské deti. "
				+ "Husté prachu kúdoly po poli valia sa, "
				+ "zem dupotom a nebo rykom sa otriasa. — "
				+ "A tá naša rodinka, tá slovänská čata, "
				+ "už vám je zôkol—vôkol od vrahov obstatá. "
				+ "Sto mečov sa každému nado hlavou blyští, "
				+ "lež Slovän nečítava vrahov na bojišti, "
				+ "ale morí. — Hoj, mor ho! detvo môjho rodu, "
				+ "kto kradmou rukou siahne na tvoju slobodu: "
				+ "a čo i tam dušu dáš v tom boji divokom: "
				+ "Mor ty len, a voľ nebyť, ako byť otrokom. " + ""
				+ "Zúri boj a našina stala si dokola: "
				+ "to vie, že premoc rímsku jej moc nepredolá: "
				+ "lež, brať moja, ak už len padnúť mi máš v boji, "
				+ "to že mi padni, ako víťazom pristojí." + ""
				+ "A môj Slovän ešte raz bystré hodil zraky "
				+ "ponad tie šíre rovne, na kraj svoj ďaleký. "
				+ "Tam na tých horách sivé mihajú sa väže, "
				+ "čo strežú slovenského národa čierťaže. "
				+ "Svätoháj, stovekými zelený lipami, "
				+ "v tajné tam lono túli bohov jeho chrámy. "
				+ "A nad riekou biely dom, v ňom jeho rod milý, "
				+ "a v čistom poli dedov posvätné mohyly: "
				+ "dedov, ktorých niekdy ľud radievali rady, "
				+ "ktorých meč divých vrahov odrážal nápady. "
				+ "Ich popoly dávno už čierna zem tam kryje, "
				+ "ale ich meno posiaľ v piesni ľudu žije. "
				+ "A sláva zašlých vekov junáka oviala; "
				+ "a duša jeho svätým ohňom splápolala, "
				+ "a meč v jeho pravici strašnejšie sa zvíja; "
				+ "bleskom blýska na vraha, hromom ho zabíja. "
				+ "Praštia zlomené raty, bité štíty zvonia "
				+ "a pyšné prilby rímske do prachu sa ronia. " + ""
				+ "Hynú i naši, hynú, ale sťa víťazi! "
				+ "Žiadna rana zvuk bôľu z úst im nevyrazí, "
				+ "vďačne lejú vernú krv po osudnom poli: "
				+ "oj, veď padnúť za národ — oj, veď to nebolí! " + ""
				+ "A boj pomaly tíchne: strašná búry sila "
				+ "divým svojím zúrením sama sa zničila. "
				+ "— A kde naši, čo bili ten Rím svetovládny, "
				+ "lebo koval úmysel na Slovänstvo zradný: — "
				+ "Kde naši? — Hojže, Tatro, jasných orlov mati! "
				+ "Nikdy sa ti tá tvoja detva už nevráti. "
				+ "Páč! Nad valným Dunajom krvavô pobrežia: "
				+ "tam ti tvoji synovia povraždení ležia. "
				+ "Neostal, ani kto by tú zvesť niesol bratom: "
				+ "Bratia vám za česť rodu padli v boji svätom. "
				+ "Lež každý na junáckej spočíva posteli, "
				+ "na kope vrahov, zbitých od jeho oceli. "
				+ "Už nežije, a ešte hrozí tá tvár bledá, "
				+ "tá ruka zmeravená meč odjať si nedá. " + ""
				+ "A cár s okom sklopeným na bojišti stojí: "
				+ "A čo? — Azda tých padlých Slovänov sa bojí? — "
				+ "Nie, lež bezdušné svojich hromady tam vidí "
				+ "a zo svojho víťazstva radovať sa stydí. "
				+ "No zahyň, studom večným zahyň, podlá duša, "
				+ "čo o slobodu dobrý ľud môj mi pokúša. "
				+ "Lež večná meno toho nech ovenčí sláva, "
				+ "kto seba v obeť svätú za svoj národ dáva. "
				+ "A ty mor ho! — hoj mor ho! detvo môjho rodu, "
				+ "kto kradmou rukou siahne na tvoju slobodu; "
				+ "a čo i tam dušu dáš v tom boji divokom: "
				+ "Mor ty len, a voľ nebyť, ako byť otrokom. ";
		// taken from:
		// http://zlatyfond.sme.sk/dielo/83/Chalupka_Mor-ho/1#ixzz1nf7uhUz5

		return convertToVS(result);
	}

	private Vector<String> initRandomEnText() {
		String result = "To be, or not to be: that is the question: "
				+ "Whether 'tis nobler in the mind to suffer "
				+ "The slings and arrows of outrageous fortune, "
				+ "Or to take arms against a sea of troubles, "
				+ "And by opposing end them? To die: to sleep; "
				+ "No more; and by a sleep to say we end "
				+ "The heart-ache and the thousand natural shocks "
				+ "That flesh is heir to, 'tis a consummation "
				+ "Devoutly to be wish'd. To die, to sleep; "
				+ "To sleep: perchance to dream: ay, there's the rub; "
				+ "For in that sleep of death what dreams may come "
				+ "When we have shuffled off this mortal coil, "
				+ "Must give us pause: there's the respect "
				+ "That makes calamity of so long life; "
				+ "For who would bear the whips and scorns of time, "
				+ "The oppressor's wrong, the proud man's contumely, "
				+ "The pangs of despised love, the law's delay, "
				+ "The insolence of office and the spurns "
				+ "That patient merit of the unworthy takes, "
				+ "When he himself might his quietus make "
				+ "With a bare bodkin? who would fardels bear, "
				+ "To grunt and sweat under a weary life, "
				+ "But that the dread of something after death, "
				+ "The undiscover'd country from whose bourn "
				+ "No traveller returns, puzzles the will "
				+ "And makes us rather bear those ills we have "
				+ "Than fly to others that we know not of? "
				+ "Thus conscience does make cowards of us all; "
				+ "And thus the native hue of resolution "
				+ "Is sicklied o'er with the pale cast of thought, "
				+ "And enterprises of great pith and moment "
				+ "With this regard their currents turn awry, "
				+ "And lose the name of action. - Soft you now! "
				+ "The fair Ophelia! Nymph, in thy orisons "
				+ "Be all my sins remember'd.";
		return convertToVS(result);
	}
}
