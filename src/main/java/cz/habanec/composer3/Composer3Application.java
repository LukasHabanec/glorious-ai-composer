package cz.habanec.composer3;

import cz.habanec.composer3.entities.Composition;
import cz.habanec.composer3.repositories.CompositionFormRepo;
import cz.habanec.composer3.repositories.CompositionRepo;
import cz.habanec.composer3.repositories.MelodyMeasureRepo;
import cz.habanec.composer3.repositories.TonalKeyRepo;
import cz.habanec.composer3.repositories.MelodyRepo;
import cz.habanec.composer3.repositories.MelodyRhythmPatternRepo;
import cz.habanec.composer3.repositories.MelodyTunePatternRepo;
import cz.habanec.composer3.repositories.ModusRepo;
import cz.habanec.composer3.repositories.QuintCircleKeyRepo;
import cz.habanec.composer3.service.CompositionCreator;
import cz.habanec.composer3.service.CompositionCreator.NewCompositionIngredients;
import cz.habanec.composer3.service.CompositionService;
import cz.habanec.composer3.service.MelodyCreator;
import cz.habanec.composer3.service.MidiPlaybackService;
import cz.habanec.composer3.service.MigrationService;
import cz.habanec.composer3.service.RhythmPatternCreator;
import cz.habanec.composer3.service.PatternService;
import cz.habanec.composer3.service.TonalKeyService;
import cz.habanec.composer3.service.TunePatternCreator;
import cz.habanec.composer3.utils.AlphabetUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@SpringBootApplication
@RequiredArgsConstructor
public class Composer3Application implements CommandLineRunner {

	private final MigrationService migrationService;
	private final MidiPlaybackService midiPlaybackService;
	private final CompositionService compositionService;
	private final TonalKeyService tonalKeyService;
	private final DataSource dataSource;
	private final ModusRepo modusRepo;
	private final QuintCircleKeyRepo quintCircleKeyRepo;
	private final CompositionRepo compositionRepo;
	private final CompositionFormRepo compositionFormRepo;
	private final MelodyRhythmPatternRepo rhythmPatternRepo;
	private final MelodyTunePatternRepo tunePatternRepo;
	private final TonalKeyRepo tonalKeyRepo;
	private final MelodyRepo melodyRepo;
	private final MelodyCreator melodyCreator;
	private final MelodyMeasureRepo melodyMeasureRepo;
	private final CompositionCreator compositionCreator;
	private final PatternService patternService;
	private final RhythmPatternCreator rhythmPatternCreator;
	private final TunePatternCreator tunePatternCreator;


	public static void main(String[] args) {
		SpringApplication.run(Composer3Application.class, args);
	}

	// done novy koncept pro tunePattern
	// - bude vznikat bez opakovani, právě 9 hodnot (pro vice not system cykleni), ale
 	// done moznost repetovat tony podle density 0 - 100 (100= vsechny stejne)
	// done vymysli, jestli radeji ukladat finalni podobu patternu do db, nebo ponechat jen raw 9-clennou verzi a k ni
	// done nove schema nesouci zaznam o repetovani, neco jako 0_2 = nulta hodnota dvakrat, 4_3 = ctvrta hodnota trikrat ???
	//todo víc než refactor accompanimentu mě zajímá vše níže - RND, začnu přes CLrunner, ať vím, co nemám, až pak FE=form,
	// accomp chci přidat na požádání, až bude melodie - chci aby reagovala na hustotu melodie,
	// spíš než na patternSchematech - ty jsou dementní, zvaž proč je nezrušit
	// todo uvaha - kazdou novou formu je treba prve ulozit, pak ji lze vybrat pro tvorbu nove skladby.
	//  - davam ji kratky nazev, mela by se pak objevit v nazvu skladby
	// todo new melody - kolik režimů? (
	//  Hook D
	//  Hybrid - pracuje s náhodnou formou ale s hook-patterny A NAOPAK
	//  Kompletně náhodně
	//  - musí být možnost psát formu ručně, vybírat z hotových, generovat i opakovaně
	//  - musí být možnost filtru povolených hodnot v rhythmPatternech
	//  - chci umožnit artikulační schéma : staccato, legato, rozličné obloučky, nátryl, trylek
	// todo exception framework - rovnou s FE
	// done uklada se mi znovu a znovu Cdur, Hdur do tonal_keys
	// done nevyzkousel jsem distribuci vice klicu do taktu - je potreba vytvorit novy opus
	// done melodie kompletně chodí, je načase ustanovit frontend, dto system, controller
	// done nextMeasureShifter mozna vubec measure nepotrebuje drzet v pameti, staci si to v creatoru predat?
	// done chci umožnit repetování tónů v rámci tunePatternu - uvazuj o skupine vice tonu!!!

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		System.out.println("Running CL-runner");
//		migrationService.execSql("db/migration/migrate-modi.sql");
//		migrationService.execSql("db/migration/migrate-quint-circle.sql");
//		migrationService.execSql("db/migration/migrate-patterns-english.sql");
//		migrationService.execSql("db/migration/migrate-form-english.sql");

//		var newForm = CompositionForm.builder()
//				.title("CL-runner")
//				.measureCount(8)
//				.keyScheme("0_0 5_-4 7_0")
//				.melodyRhythmScheme("AABACCBA")
//				.melodyTuneScheme("AABACCBA")
//				.build();

		migrationService.removeWhitespacesFromAllHookPatterns();
//newFromRandom();

	}


	public void newFromRandom() {
		final int[] RHYTHM_GRANULARITY_OPTIONS = {8, 4, 2};
		final int[] TUNE_REPETITION_DENSITY_OPTIONS = {20, 40, 70};
		final TunePatternCreator.Eccentricity[] TUNE_ECCENTRICITY_OPTIONS = {
				TunePatternCreator.Eccentricity.NO_ECCENTRICITY,
				TunePatternCreator.Eccentricity.MID_ECCENTRICITY,
				TunePatternCreator.Eccentricity.HIGH_ECCENTRICITY
		};

		var form = compositionFormRepo.findByTitle("CL-runner").orElseThrow();
		var keyAmajor = tonalKeyService.getTonalKeyByLabels("A", "MAJOR");

		var rnd = new Random();
		List<String> rhythmPatternsRaw = new ArrayList<>();
		for (int option : RHYTHM_GRANULARITY_OPTIONS) {
			var newPattern = rhythmPatternCreator.createRandomRhythmPatternByCutting(
					4, rnd.nextInt(1, 17), option);
			if (checkUniqueness(newPattern, rhythmPatternsRaw)) {
				rhythmPatternsRaw.add(newPattern);
			}
		}
		List<String> tunePatternsRaw = new ArrayList<>();
		for (TunePatternCreator.Eccentricity e : TUNE_ECCENTRICITY_OPTIONS) {
			var newPattern = tunePatternCreator.createTunePattern(
					rnd.nextInt(2, 9), rnd.nextInt(2, 9), e);
			if (checkUniqueness(newPattern, tunePatternsRaw)) {
				tunePatternsRaw.add(newPattern);
			}
		}

//		var rhythmPatternsRaw = List.of("4,2,2,1,1,2,4", "1,1,2,4,8", "2,2,2,2,3,1,3,1"); // toto je fajn, bo z FE budou chodit takoveto stringy
		var rhythmPatterns = rhythmPatternsRaw.stream()
				.map(string -> patternService.getOrCreateMelodyRhythmPattern(string, form.getId()))
				.toList();

//		var tunePatternsRaw = List.of("0,1,2,3,4,5,1,2,0", "0,-1,-2,-3,-5,1,0,-1,-2", "0,4,0,4,3,1,1,3,5");
		var tunePatterns = tunePatternsRaw.stream()
				.map(string -> patternService.getOrCreateMelodyTunePattern(string, form.getId()))
				.toList();

		var repetitionPatterns = rhythmPatterns.stream()
				.map(pattern -> tunePatternCreator.createRepetitionPattern(
						pattern.getValues().size(),
						rnd.nextInt(101)))
				.toList();

		var title = AlphabetUtils.generateRandomName();
		var tempo = 100;

		var composition = compositionCreator.createNewComposition(NewCompositionIngredients.builder()
				.rhythmPatterns(rhythmPatterns)
				.tunePatterns(tunePatterns)
				.repetitionPatterns(repetitionPatterns)
				.mainKey(keyAmajor)
				.form(form)
				.title(title)
				.tempo(tempo)
				.build());
	}

//		var composition = migrationService.migrateOldCompositionFrom(
//				MigrationService.MigratingCompositionIngredients.builder()
//						.formName(MigrationService.FOUR_MEASURE_TEST_FORM_KEY)
//						.mainKeyIndex(8)
//						.startingGrade(0)
//						.tempo(120)
//						.shuffledRhythm("19 20 22 2 12 3 11 9 15 6 21 14 23 16 17 0 13 1 7 4 24 10 8 5 18")
//						.shuffledTune("9 31 34 0 47 28 3 14 25 19 12 35 26 17 43 37 46 18 33 32 40 20 15 13 24 23 30 27 10 42 21")
//						.shifters("0 0 0 0")
//						.hash("Four")
//						.build()
//		);

//		var composition = migrationService.migrateOldCompositionFrom(
//				MigrationService.MigratingCompositionIngredients.builder()
//						.formName(MigrationService.HOOK_FORM_KEY)
//						.mainKeyIndex(8)
//						.startingGrade(0)
//						.tempo(120)
//						.shuffledRhythm("19 20 22 2 12 3 11 9 15 6 21 14 23 16 17 0 13 1 7 4 24 10 8 5 18")
//						.shuffledTune("9 31 34 0 47 28 3 14 25 19 12 35 26 17 43 37 46 18 33 32 40 20 15 13 24 23 30 27 10 42 21")
//						.shifters("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0")
//						.hash("Abcd")
//						.build()
//		);



	private boolean checkUniqueness(String newPattern, List<String> rhythmPatterns) {
		return rhythmPatterns.stream().noneMatch(newPattern::equals);
	}

}
