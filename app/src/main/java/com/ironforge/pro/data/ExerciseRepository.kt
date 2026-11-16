package com.ironforge.pro.data

import com.ironforge.pro.model.*

object ExerciseRepository {

    val allExercises: List<Exercise> = listOf(
        Exercise(
            id = "chest_bench_barbell",
            name = "Supino Reto com Barra Olímpica",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.BENCH_PRESS,
            primaryTarget = "Peitoral Maior (Porção Esternocostal)",
            synergists = listOf("Deltoide Anterior", "Tríceps Braquial", "Serrátil Anterior"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "5–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 180,
            executionSteps = listOf(
                "Deite-se no banco com os olhos alinhados sob a barra e pés firmes no chão.",
                "Retraia e deprima as escápulas formando arco torácico estável.",
                "Desça a barra controladamente até tocar a linha inferior do esterno com cotovelos a ~45-60°.",
                "Empurre explosivamente mantendo escápulas travadas."
            ),
            biomechanicsTips = listOf(
                "Mantenha punhos neutros alinhados aos antebraços.",
                "Use leg drive empurrando o solo com os pés."
            ),
            commonMistakes = listOf(
                "Abrir cotovelos a 90° sobrecarregando o ombro.",
                "Quicar a barra no tórax."
            )
        ),
        Exercise(
            id = "chest_bench_dumbbell",
            name = "Supino Reto com Halteres",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.BENCH_PRESS,
            primaryTarget = "Peitoral Maior (Amplitude Livre Esternal)",
            synergists = listOf("Deltoide Anterior", "Tríceps Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Deite-se no banco plano com halteres alinhados ao meio do peito.",
                "Desça os halteres além da linha do tronco buscando alongamento profundo.",
                "Empurre convergindo os halteres no topo sem bater um no outro."
            ),
            biomechanicsTips = listOf(
                "Maior liberdade articular para os ombros comparado à barra.",
                "Corrige assimetrias entre lados."
            ),
            commonMistakes = listOf(
                "Deixar os cotovelos abrirem excessivamente.",
                "Encurtar a descida."
            )
        ),
        Exercise(
            id = "chest_incline_barbell",
            name = "Supino Inclinado com Barra (30°)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.INCLINE_PRESS,
            primaryTarget = "Peitoral Maior (Porção Clavicular / Superior)",
            synergists = listOf("Deltoide Anterior", "Tríceps Braquial"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "6–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 150,
            executionSteps = listOf(
                "Ajuste o banco a 30° e posicione-se com escápulas retraídas.",
                "Desça a barra até a porção superior do tórax (abaixo da clavícula).",
                "Empurre verticalmente até quase estender os cotovelos."
            ),
            biomechanicsTips = listOf(
                "Inclinação de 30° maximiza o feixe clavicular sem excesso de deltoide anterior."
            ),
            commonMistakes = listOf(
                "Usar banco a 45-60°.",
                "Tirar o quadril do assento."
            )
        ),
        Exercise(
            id = "chest_incline_dumbbell",
            name = "Supino Inclinado com Halteres (30°)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.INCLINE_PRESS,
            primaryTarget = "Peitoral Maior (Feixe Clavicular Superior)",
            synergists = listOf("Deltoide Anterior", "Tríceps Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Ajuste o banco em 30° e impulsione os halteres com as coxas.",
                "Desça os halteres ao lado do peitoral superior alongando ao máximo.",
                "Empurre convergindo suavemente no topo."
            ),
            biomechanicsTips = listOf(
                "Considerado um dos melhores construtores de peitoral superior."
            ),
            commonMistakes = listOf(
                "Encurtar a fase excêntrica.",
                "Bater os halteres no topo."
            )
        ),
        Exercise(
            id = "chest_incline_machine",
            name = "Supino Inclinado Articulado na Máquina",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.INCLINE_PRESS,
            primaryTarget = "Peitoral Maior (Porção Clavicular)",
            synergists = listOf("Deltoide Anterior", "Tríceps Braquial"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-0-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Ajuste o assento para alinhar as manoplas com a parte superior do peito.",
                "Empurre com trajetória convergente mantendo ombros colados no encosto.",
                "Controle a volta por 3 segundos."
            ),
            biomechanicsTips = listOf(
                "Estabilidade total permite treinar até a falha técnica com segurança."
            ),
            commonMistakes = listOf(
                "Projetar os ombros para frente no final do movimento."
            )
        ),
        Exercise(
            id = "chest_decline_barbell",
            name = "Supino Declinado com Barra / Máquina",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.BENCH_PRESS,
            primaryTarget = "Peitoral Maior (Porção Esternal Inferior)",
            synergists = listOf("Tríceps Braquial", "Deltoide Anterior"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-0-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Trave as pernas no apoio do banco declinado (-15°).",
                "Desça a barra até a linha inferior do peitoral.",
                "Empurre mantendo controle total."
            ),
            biomechanicsTips = listOf(
                "Menor tensão no deltoide anterior comparado ao supino reto."
            ),
            commonMistakes = listOf(
                "Usar declinação exagerada causando desconforto cefálico."
            )
        ),
        Exercise(
            id = "chest_dumbbell_fly",
            name = "Crucifixo Reto com Halteres",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.FLY_CROSSOVER,
            primaryTarget = "Peitoral Maior (Alongamento Sob Carga)",
            synergists = listOf("Deltoide Anterior"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Deitado no banco plano, mantenha leve flexão fixa nos cotovelos (15°).",
                "Abra os braços em arco amplo até sentir alongamento profundo no peitoral.",
                "Retorne até os halteres alinharem acima dos ombros (onde ainda há tensão)."
            ),
            biomechanicsTips = listOf(
                "Não encoste os halteres no topo para não perder a tensão gravitacional."
            ),
            commonMistakes = listOf(
                "Dobrar demais os cotovelos transformando em supino."
            )
        ),
        Exercise(
            id = "chest_cable_crossover_high",
            name = "Crucifixo na Polia Alta (Crossover)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.FLY_CROSSOVER,
            primaryTarget = "Peitoral Maior (Fibras Inferiores e Esternais)",
            synergists = listOf("Deltoide Anterior"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 75,
            executionSteps = listOf(
                "Posicione as polias acima da cabeça e dê um passo à frente.",
                "Mantenha cotovelos semiflexionados e traga os pegadores até cruzar na frente do umbigo.",
                "Segure 2 segundos no pico de contração."
            ),
            biomechanicsTips = listOf(
                "Tensão contínua no ponto de contração máxima."
            ),
            commonMistakes = listOf(
                "Flexionar e estender cotovelos durante a série."
            )
        ),
        Exercise(
            id = "chest_cable_crossover_low",
            name = "Crucifixo na Polia Baixa (Fibras Superiores)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.FLY_CROSSOVER,
            primaryTarget = "Peitoral Maior (Porção Clavicular)",
            synergists = listOf("Deltoide Anterior", "Bíceps Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 75,
            executionSteps = listOf(
                "Ajuste as polias na posição mais baixa e segure os pegadores com palmas para cima.",
                "Eleve os braços à frente e para dentro até a altura do queixo/nariz.",
                "Contraia o peitoral superior no topo e desça lentamente."
            ),
            biomechanicsTips = listOf(
                "Excelente isolador para a região clavicular do peito."
            ),
            commonMistakes = listOf(
                "Usar impulso de lombar para subir a carga."
            )
        ),
        Exercise(
            id = "chest_pec_deck",
            name = "Voador / Peck Deck na Máquina",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.FLY_CROSSOVER,
            primaryTarget = "Peitoral Maior (Adução Horizontal)",
            synergists = listOf("Deltoide Anterior"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-0-1-2",
            restSeconds = 75,
            executionSteps = listOf(
                "Ajuste o banco para manoplas na altura média do peitoral.",
                "Feche os braços até unir as manoplas mantendo ombros baixos.",
                "Controle a volta em 3 segundos."
            ),
            biomechanicsTips = listOf(
                "Isolamento puro com curva de resistência constante."
            ),
            commonMistakes = listOf(
                "Subir os ombros em direção às orelhas."
            )
        ),
        Exercise(
            id = "chest_dips_weighted",
            name = "Paralelas Foco Peitoral (Chest Dips)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.DIPS,
            primaryTarget = "Peitoral Maior (Porção Costal Inferior)",
            synergists = listOf("Tríceps Braquial", "Deltoide Anterior"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "6–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Incline o tronco ~30° à frente e abra levemente os cotovelos.",
                "Desça até os ombros ficarem ligeiramente abaixo dos cotovelos.",
                "Empurre mantendo o tronco inclinado."
            ),
            biomechanicsTips = listOf(
                "Adicione cinto de carga quando superar 12 reps."
            ),
            commonMistakes = listOf(
                "Manter tronco reto foca apenas no tríceps."
            )
        ),
        Exercise(
            id = "chest_weighted_pushup",
            name = "Flexão de Braços com Sobrecarga / Déficit",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.BENCH_PRESS,
            primaryTarget = "Peitoral Maior e Serrátil Anterior",
            synergists = listOf("Tríceps Braquial", "Deltoide Anterior", "Core"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–20 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 75,
            executionSteps = listOf(
                "Apoie as mãos em steps ou pegadores para aumentar a amplitude.",
                "Desça o peito abaixo da linha das mãos mantendo o corpo alinhado.",
                "Empurre até prostração completa das escápulas."
            ),
            biomechanicsTips = listOf(
                "Permite movimento natural das escápulas sobre a caixa torácica."
            ),
            commonMistakes = listOf(
                "Deixar o quadril cair hiperestendendo a lombar."
            )
        ),
        Exercise(
            id = "back_pull_up",
            name = "Barra Fixa Pronada (Pull-Up)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.PULL_UP,
            primaryTarget = "Latíssimo do Dorso e Redondo Maior",
            synergists = listOf("Bíceps Braquial", "Braquiorradial", "Trapézio Inferior"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "6–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Segure a barra com pegada pronada aberta.",
                "Inicie deprimindo as escápulas e puxe o peito em direção à barra.",
                "Desça até extensão completa dos braços."
            ),
            biomechanicsTips = listOf(
                "Imagine puxar os cotovelos para dentro dos bolsos."
            ),
            commonMistakes = listOf(
                "Fazer meias repetições sem estender os cotovelos."
            )
        ),
        Exercise(
            id = "back_chin_up",
            name = "Barra Fixa Supinada (Chin-Up)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.PULL_UP,
            primaryTarget = "Latíssimo do Dorso Inferior e Bíceps Braquial",
            synergists = listOf("Redondo Maior", "Braquial", "Romboides"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "6–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Segure a barra com palmas voltadas para você na largura dos ombros.",
                "Puxe até o queixo passar a barra mantendo o peito alto.",
                "Controle a descida por 3 segundos."
            ),
            biomechanicsTips = listOf(
                "Maior vantagem mecânica para o bíceps e fibras inferiores do latíssimo."
            ),
            commonMistakes = listOf(
                "Balançar as pernas para ganhar impulso."
            )
        ),
        Exercise(
            id = "back_lat_pulldown",
            name = "Puxada Frontal Pegada Pronada Aberta",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.LAT_PULLDOWN,
            primaryTarget = "Latíssimo do Dorso (Largura Dorsal)",
            synergists = listOf("Redondo Maior", "Romboides", "Bíceps Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Trave as coxas no apoio e segure a barra a ~1,3x a largura dos ombros.",
                "Puxe a barra até a parte superior do esterno inclinando apenas 10–15°.",
                "Estenda completamente os braços no topo."
            ),
            biomechanicsTips = listOf(
                "Use pegada sem polegar ou straps se o antebraço cansar antes."
            ),
            commonMistakes = listOf(
                "Puxar atrás da nuca.",
                "Balançar o tronco para trás."
            )
        ),
        Exercise(
            id = "back_lat_pulldown_neutral",
            name = "Puxada Frontal Pegada Triângulo / Neutra",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.LAT_PULLDOWN,
            primaryTarget = "Latíssimo do Dorso (Plano Sagital)",
            synergists = listOf("Braquial", "Trapézio Inferior", "Romboides"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–12 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Use pegador triângulo ou barra neutra paralela.",
                "Puxe os cotovelos rentes ao tronco até o pegador tocar o esterno.",
                "Alongue completamente as dorsais no topo."
            ),
            biomechanicsTips = listOf(
                "Excelente ativação das fibras ilíacas e lombares do latíssimo."
            ),
            commonMistakes = listOf(
                "Arredondar os ombros para frente no final da puxada."
            )
        ),
        Exercise(
            id = "back_single_arm_pulldown",
            name = "Puxada Unilateral na Polia Alta (Iliac Pulldown)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.LAT_PULLDOWN,
            primaryTarget = "Latíssimo do Dorso (Fibras Ilíacas Inferiores)",
            synergists = listOf("Redondo Maior", "Bíceps Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "10–14 reps / lado",
            tempoTUT = "2-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Ajoelhe-se ou sente-se de lado para a polia alta segurando o pegador D.",
                "Puxe o cotovelo em direção ao quadril mantendo o tronco estável.",
                "Permita que o braço suba totalmente alongando a lateral das costas."
            ),
            biomechanicsTips = listOf(
                "Alinha perfeitamente o vetor do cabo com as fibras inferiores do latíssimo."
            ),
            commonMistakes = listOf(
                "Girar o tronco para ajudar na puxada."
            )
        ),
        Exercise(
            id = "back_barbell_row",
            name = "Remada Curvada com Barra (Pronada)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Latíssimo do Dorso, Romboides e Trapézio Médio",
            synergists = listOf("Deltoide Posterior", "Eretores da Espinha", "Bíceps"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "6–10 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 150,
            executionSteps = listOf(
                "Incline o tronco a ~40° mantendo a coluna rigorosamente neutra.",
                "Puxe a barra em direção ao umbigo fechando as escápulas no topo.",
                "Controle a descida sem perder o bracing abdominal."
            ),
            biomechanicsTips = listOf(
                "Construtor fundamental de densidade e espessura dorsal."
            ),
            commonMistakes = listOf(
                "Arredondar a lombar sob fadiga."
            )
        ),
        Exercise(
            id = "back_pendlay_row",
            name = "Remada Pendlay Explosiva com Barra",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Dorsais, Trapézio Médio e Potência Posterior",
            synergists = listOf("Deltoide Posterior", "Bíceps", "Core"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Avançado",
            recommendedReps = "5–8 reps",
            tempoTUT = "1-0-X-1",
            restSeconds = 150,
            executionSteps = listOf(
                "Com tronco quase paralelo ao solo, parta com a barra apoiada no chão a cada repetição.",
                "Puxe explosivamente até a barra tocar a base do esterno.",
                "Retorne a barra ao solo, resete a postura e repita."
            ),
            biomechanicsTips = listOf(
                "Elimina impulso entre repetições e desenvolve força explosiva."
            ),
            commonMistakes = listOf(
                "Levantar o tronco durante a puxada."
            )
        ),
        Exercise(
            id = "back_one_arm_dumbbell_row",
            name = "Remada Unilateral com Halter (Serrote)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Latíssimo do Dorso e Redondo Maior",
            synergists = listOf("Romboides", "Deltoide Posterior", "Bíceps"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps / lado",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Apoie uma mão e um joelho no banco (ou mão no rack em base tripla).",
                "Deixe o ombro descer no fundo para alongar a escápula.",
                "Puxe o halter em arco em direção ao quadril mantendo o cotovelo próximo ao corpo."
            ),
            biomechanicsTips = listOf(
                "Puxar em direção ao quadril (e não ao ombro) isola o latíssimo."
            ),
            commonMistakes = listOf(
                "Torcer a coluna excessivamente no topo."
            )
        ),
        Exercise(
            id = "back_seated_cable_row",
            name = "Remada Baixa Sentada na Polia (Triângulo)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Espessura Dorsal (Romboides, Trapézio e Latíssimo)",
            synergists = listOf("Deltoide Posterior", "Bíceps", "Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–12 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Apoie os pés na plataforma com joelhos semiflexionados e coluna ereta.",
                "Abra as escápulas à frente no início para alongar o meio das costas.",
                "Puxe o triângulo até o abdômen retraindo as escápulas por 1 segundo."
            ),
            biomechanicsTips = listOf(
                "Mantenha cotovelos raspando nas costelas."
            ),
            commonMistakes = listOf(
                "Usar gangorra lombar excessiva."
            )
        ),
        Exercise(
            id = "back_chest_supported_row",
            name = "Remada Cavalinho / Apoiada no Peito",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Trapézio Médio, Romboides e Latíssimo",
            synergists = listOf("Deltoide Posterior", "Bíceps Braquial"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 120,
            executionSteps = listOf(
                "Apoie o tórax no encosto eliminando fadiga lombar.",
                "Puxe as manoplas fechando as escápulas no topo por 1 segundo.",
                "Estenda completamente os braços na descida."
            ),
            biomechanicsTips = listOf(
                "Permite levar as costas à falha mecânica real com total segurança."
            ),
            commonMistakes = listOf(
                "Descolar o peito do apoio para roubar."
            )
        ),
        Exercise(
            id = "back_straight_arm_pulldown",
            name = "Pulldown Braços Estendidos na Polia",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.STRAIGHT_ARM_PULL,
            primaryTarget = "Latíssimo do Dorso (Isolamento)",
            synergists = listOf("Redondo Maior", "Tríceps (Cabeça Longa)"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 60,
            executionSteps = listOf(
                "Incline o tronco ~25° frente à polia alta com barra ou corda.",
                "Com cotovelos quase estendidos, puxe em arco até as coxas.",
                "Contraia por 2 segundos e retorne até acima da cabeça."
            ),
            biomechanicsTips = listOf(
                "Isola as dorsais sem fadigar o bíceps."
            ),
            commonMistakes = listOf(
                "Dobrar os cotovelos transformando em tríceps pulley."
            )
        ),
        Exercise(
            id = "back_deadlift_conventional",
            name = "Levantamento Terra Convencional",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.DEADLIFT,
            primaryTarget = "Cadeia Posterior Completa (Eretores, Glúteos, Posteriores e Trapézio)",
            synergists = listOf("Quadríceps", "Dorsais", "Antebraços", "Core"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Avançado",
            recommendedReps = "3–6 reps",
            tempoTUT = "2-1-1-0",
            restSeconds = 210,
            executionSteps = listOf(
                "Pés na largura dos quadris, barra sobre o meio do pé.",
                "Tire a folga da barra, trave o peito alto e empurre o chão com as pernas.",
                "Mantenha a barra colada às canelas e coxas até travar quadril e joelhos."
            ),
            biomechanicsTips = listOf(
                "Principal exercício de força bruta do corpo humano."
            ),
            commonMistakes = listOf(
                "Arredondar a lombar.",
                "Afastar a barra das pernas."
            )
        ),
        Exercise(
            id = "back_rack_pull",
            name = "Meio-Terra no Rack (Rack Pull)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.DEADLIFT,
            primaryTarget = "Trapézio Superior/Médio, Romboides e Eretores",
            synergists = listOf("Glúteos", "Antebraços", "Dorsais"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "5–8 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 180,
            executionSteps = listOf(
                "Posicione a barra nos pinos do rack na altura dos joelhos.",
                "Segure com straps, trave as escápulas e estenda o quadril.",
                "Controle a descida até apoiar nos pinos."
            ),
            biomechanicsTips = listOf(
                "Permite sobrecarga supramáxima na parte superior das costas."
            ),
            commonMistakes = listOf(
                "Hiperestender a lombar no topo."
            )
        ),
        Exercise(
            id = "shld_barbell_ohp",
            name = "Desenvolvimento Militar com Barra em Pé (OHP)",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.OVERHEAD_PRESS,
            primaryTarget = "Deltoide Anterior e Lateral + Estabilidade Core",
            synergists = listOf("Tríceps Braquial", "Trapézio Superior", "Serrátil"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "5–8 reps",
            tempoTUT = "2-0-1-0",
            restSeconds = 150,
            executionSteps = listOf(
                "Em pé com glúteos e abdômen travados, apoie a barra na clavícula.",
                "Empurre verticalmente passando a barra rente ao nariz e encaixando a cabeça à frente no topo.",
                "Desça controladamente até a clavícula."
            ),
            biomechanicsTips = listOf(
                "Contraia os glúteos o tempo todo para blindar a lombar."
            ),
            commonMistakes = listOf(
                "Inclinar o tronco para trás fazendo um supino em pé."
            )
        ),
        Exercise(
            id = "shld_overhead_press_dumbbell",
            name = "Desenvolvimento Militar com Halteres Sentado",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.OVERHEAD_PRESS,
            primaryTarget = "Deltoide Anterior e Lateral",
            synergists = listOf("Tríceps Braquial", "Trapézio Superior", "Serrátil"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-0-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Banco a 80–85°, halteres na altura das orelhas com cotovelos 30° à frente (plano escapular).",
                "Empurre acima da cabeça sem bater os halteres no topo.",
                "Desça até alinhar com o queixo."
            ),
            biomechanicsTips = listOf(
                "O plano escapular protege os tendões do manguito rotador."
            ),
            commonMistakes = listOf(
                "Encurtar a descida parando no topo da cabeça."
            )
        ),
        Exercise(
            id = "shld_arnold_press",
            name = "Desenvolvimento Arnold com Halteres",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.OVERHEAD_PRESS,
            primaryTarget = "Deltoide Anterior e Lateral (Rotação Completa)",
            synergists = listOf("Tríceps Braquial", "Serrátil Anterior"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "10–12 reps",
            tempoTUT = "3-0-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Inicie com halteres à frente dos ombros e palmas voltadas para você.",
                "Ao empurrar acima da cabeça, gire os punhos externamente terminando com palmas para frente.",
                "Inverta a rotação na descida."
            ),
            biomechanicsTips = listOf(
                "Aumenta o tempo sob tensão nos feixes anterior e lateral."
            ),
            commonMistakes = listOf(
                "Girar rápido demais sem controle escapular."
            )
        ),
        Exercise(
            id = "shld_machine_press",
            name = "Desenvolvimento na Máquina Articulada",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.OVERHEAD_PRESS,
            primaryTarget = "Deltoide Anterior e Lateral",
            synergists = listOf("Tríceps Braquial"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–14 reps",
            tempoTUT = "3-0-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Ajuste o banco para manoplas na altura dos ombros.",
                "Empurre até quase estender os cotovelos.",
                "Controle a descida por 3 segundos."
            ),
            biomechanicsTips = listOf(
                "Ótimo para finalizar ombros com segurança."
            ),
            commonMistakes = listOf(
                "Descolar a lombar do encosto."
            )
        ),
        Exercise(
            id = "shld_lateral_raise_dumbbell",
            name = "Elevação Lateral com Halteres",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.LATERAL_RAISE,
            primaryTarget = "Deltoide Lateral (Porção Acromial — Largura)",
            synergists = listOf("Supraespinal", "Trapézio Superior"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–20 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Incline o tronco 10° à frente e eleve os braços no plano escapular até a altura dos ombros.",
                "Lidere com os cotovelos mantendo punhos neutros.",
                "Desça resistindo sem descansar os halteres nas coxas."
            ),
            biomechanicsTips = listOf(
                "Pense em empurrar os halteres para as paredes laterais."
            ),
            commonMistakes = listOf(
                "Balançar o quadril.",
                "Encolher o trapézio antes de abduzir."
            )
        ),
        Exercise(
            id = "shld_cable_lateral_raise",
            name = "Elevação Lateral Unilateral na Polia Baixa",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.LATERAL_RAISE,
            primaryTarget = "Deltoide Lateral (Tensão Constante)",
            synergists = listOf("Supraespinal", "Trapézio Médio"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "12–16 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Polia na altura do punho, segure a estrutura com a mão livre e incline-se levemente.",
                "Abduza o braço até a horizontal com tensão desde o início.",
                "Controle a descida."
            ),
            biomechanicsTips = listOf(
                "Maximiza a tensão na posição alongada do deltoide lateral."
            ),
            commonMistakes = listOf(
                "Girar o tronco durante a subida."
            )
        ),
        Exercise(
            id = "shld_machine_lateral_raise",
            name = "Elevação Lateral na Máquina",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.LATERAL_RAISE,
            primaryTarget = "Deltoide Lateral (Isolamento Total)",
            synergists = listOf("Supraespinal"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–18 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Apoie os cotovelos nos rolos alinhando o eixo do ombro com a máquina.",
                "Eleve até a paralela ao chão e segure 1 segundo.",
                "Desça lentamente."
            ),
            biomechanicsTips = listOf(
                "Elimina qualquer compensação de punho ou antebraço."
            ),
            commonMistakes = listOf(
                "Subir os ombros para as orelhas."
            )
        ),
        Exercise(
            id = "shld_front_raise",
            name = "Elevação Frontal com Barra W ou Anilha",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.OVERHEAD_PRESS,
            primaryTarget = "Deltoide Anterior (Porção Clavicular)",
            synergists = listOf("Peitoral Superior", "Serrátil"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Em pé com tronco firme, eleve a barra ou anilha à frente até a linha dos olhos.",
                "Controle a descida sem balançar o quadril."
            ),
            biomechanicsTips = listOf(
                "Mantenha cotovelos levemente flexionados."
            ),
            commonMistakes = listOf(
                "Jogar o corpo para trás para subir o peso."
            )
        ),
        Exercise(
            id = "shld_reverse_pec_deck",
            name = "Crucifixo Invertido na Máquina (Posterior)",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.REVERSE_FLY,
            primaryTarget = "Deltoide Posterior",
            synergists = listOf("Infraespinal", "Redondo Menor", "Romboides"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–18 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Peito apoiado no encosto, segure as manoplas horizontais.",
                "Abra os braços para trás em arco mantendo ombros baixos.",
                "Pare na linha do tronco e retorne lentamente."
            ),
            biomechanicsTips = listOf(
                "Evite fechar demais as escápulas para o trapézio não roubar."
            ),
            commonMistakes = listOf(
                "Usar carga alta gerando impulso cervical."
            )
        ),
        Exercise(
            id = "shld_bent_over_rear_delt",
            name = "Crucifixo Invertido Inclinado com Halteres",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.REVERSE_FLY,
            primaryTarget = "Deltoide Posterior",
            synergists = listOf("Romboides", "Trapézio Médio", "Infraespinal"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "12–20 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Incline o tronco a ~45° (ou apoie o peito em banco inclinado).",
                "Abra os halteres para os lados e levemente para trás liderando com os cotovelos.",
                "Controle a descida."
            ),
            biomechanicsTips = listOf(
                "Apoiar o peito no banco inclinado elimina balanço lombar."
            ),
            commonMistakes = listOf(
                "Subir os pesos com impulso de coluna."
            )
        ),
        Exercise(
            id = "shld_face_pull",
            name = "Face Pull na Polia Alta com Corda",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.REVERSE_FLY,
            primaryTarget = "Deltoide Posterior e Manguito Rotador",
            synergists = listOf("Trapézio Médio e Inferior", "Romboides"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "15–20 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 60,
            executionSteps = listOf(
                "Polia na altura do rosto, puxe a corda em direção à testa abrindo as pontas.",
                "Faça rotação externa terminando em pose de duplo bíceps.",
                "Segure 2 segundos no pico."
            ),
            biomechanicsTips = listOf(
                "Essencial para saúde articular dos ombros e postura."
            ),
            commonMistakes = listOf(
                "Puxar a corda para o peito sem rotação externa."
            )
        ),
        Exercise(
            id = "bi_straight_bar_curl",
            name = "Rosca Direta com Barra Reta Olímpica",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Bíceps Braquial (Supinação Máxima)",
            synergists = listOf("Braquial", "Braquiorradial"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Segure a barra reta na largura dos ombros com palmas totalmente supinadas.",
                "Flexione os cotovelos mantendo-os fixos ao lado das costelas.",
                "Desça em 3 segundos até extensão quase completa."
            ),
            biomechanicsTips = listOf(
                "A supinação completa ativa fortemente a cabeça curta e longa."
            ),
            commonMistakes = listOf(
                "Jogar os cotovelos para frente na subida."
            )
        ),
        Exercise(
            id = "bi_barbell_ez_curl",
            name = "Rosca Direta com Barra W",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Bíceps Braquial (Cabeças Longa e Curta)",
            synergists = listOf("Braquial", "Braquiorradial"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Segure a barra W na curvatura ergonômica e flexione até contração total.",
                "Contraia por 1 segundo no topo e desça lentamente."
            ),
            biomechanicsTips = listOf(
                "Reduz estresse articular nos punhos comparado à barra reta."
            ),
            commonMistakes = listOf(
                "Balançar o tronco para trás."
            )
        ),
        Exercise(
            id = "bi_alternating_dumbbell_curl",
            name = "Rosca Alternada com Halteres Supinando",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Bíceps Braquial (Flexão + Supinação Ativa)",
            synergists = listOf("Braquial", "Supinador"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–12 reps / braço",
            tempoTUT = "2-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Inicie com halteres em pegada neutra ao lado do corpo.",
                "À medida que flexiona o cotovelo, gire o punho para fora (supinação) elevando o dedo mindinho.",
                "Desça controladamente alternando os braços."
            ),
            biomechanicsTips = listOf(
                "Explora as duas funções principais do bíceps: flexão e supinação."
            ),
            commonMistakes = listOf(
                "Girar o tronco a cada repetição."
            )
        ),
        Exercise(
            id = "bi_incline_dumbbell_curl",
            name = "Rosca Inclinada com Halteres no Banco (45–60°)",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Bíceps Braquial (Ênfase Cabeça Longa — Pico)",
            synergists = listOf("Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "10–14 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Banco inclinado a 45–60°, deixe os braços pendurados atrás da linha do tronco.",
                "Flexione os cotovelos sem trazê-los para frente.",
                "Desça totalmente alongando a cabeça longa."
            ),
            biomechanicsTips = listOf(
                "Estiramento máximo sob carga potencializa a hipertrofia."
            ),
            commonMistakes = listOf(
                "Avançar os cotovelos para frente na subida."
            )
        ),
        Exercise(
            id = "bi_bayesian_cable_curl",
            name = "Rosca Bayesiana Unilateral na Polia Baixa",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Bíceps Braquial (Estiramento Sob Tensão Contínua)",
            synergists = listOf("Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "10–15 reps / braço",
            tempoTUT = "3-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "De costas para a polia baixa, dê um passo à frente deixando o braço estendido atrás do corpo.",
                "Flexione o cotovelo mantendo-o atrás do tronco.",
                "Retorne até extensão total."
            ),
            biomechanicsTips = listOf(
                "Um dos exercícios mais eficientes para hipertrofia mediada pelo alongamento."
            ),
            commonMistakes = listOf(
                "Puxar o cotovelo à frente do tronco."
            )
        ),
        Exercise(
            id = "bi_preacher_curl",
            name = "Rosca Scott na Máquina ou Banco W",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.PREACHER_CURL,
            primaryTarget = "Bíceps Braquial (Ênfase Cabeça Curta)",
            synergists = listOf("Braquial"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Encaixe as axilas no apoio e flexione até próximo de 90°.",
                "Desça lentamente até quase estender o cotovelo sem soltar o peso."
            ),
            biomechanicsTips = listOf(
                "Bloqueia totalmente a ajuda dos ombros."
            ),
            commonMistakes = listOf(
                "Hiperestender bruscamente o cotovelo no fundo."
            )
        ),
        Exercise(
            id = "bi_concentration_curl",
            name = "Rosca Concentrada Unilateral",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.PREACHER_CURL,
            primaryTarget = "Bíceps Braquial (Pico de Contração)",
            synergists = listOf("Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps / braço",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Sentado, apoie o tríceps na face interna da coxa.",
                "Flexione o halter em direção ao ombro supinando o punho.",
                "Segure 1 segundo no topo."
            ),
            biomechanicsTips = listOf(
                "Conexão mente-músculo máxima."
            ),
            commonMistakes = listOf(
                "Levantar o cotovelo da coxa."
            )
        ),
        Exercise(
            id = "bi_hammer_curl",
            name = "Rosca Martelo com Halteres",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Braquial e Braquiorradial (Espessura do Braço)",
            synergists = listOf("Bíceps Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–14 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Segure os halteres em pegada neutra e flexione os cotovelos sem girar o punho.",
                "Suba até a altura do ombro e desça controladamente."
            ),
            biomechanicsTips = listOf(
                "O braquial empurra o bíceps para cima aumentando a circunferência do braço."
            ),
            commonMistakes = listOf(
                "Supinar o punho na subida."
            )
        ),
        Exercise(
            id = "bi_rope_hammer_curl",
            name = "Rosca Martelo na Polia Baixa com Corda",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Braquial, Braquiorradial e Bíceps",
            synergists = listOf("Antebraços"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Polia baixa com corda, puxe mantendo polegares para cima.",
                "Contraia no topo e controle a volta."
            ),
            biomechanicsTips = listOf(
                "Tensão constante em todo o arco de movimento."
            ),
            commonMistakes = listOf(
                "Inclinar o tronco para trás."
            )
        ),
        Exercise(
            id = "tri_close_grip_bench",
            name = "Supino Fechado com Barra",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.BENCH_PRESS,
            primaryTarget = "Tríceps Braquial (Sobrecarga Mecânica Pesada)",
            synergists = listOf("Peitoral Maior", "Deltoide Anterior"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "6–10 reps",
            tempoTUT = "3-0-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Pegada na largura exata dos ombros, desça a barra com cotovelos rentes ao tronco.",
                "Empurre focando na extensão dos cotovelos."
            ),
            biomechanicsTips = listOf(
                "Principal exercício composto para progredir carga pesada no tríceps."
            ),
            commonMistakes = listOf(
                "Pegada estreita demais forçando os punhos."
            )
        ),
        Exercise(
            id = "tri_dips_upright",
            name = "Paralelas Foco Tríceps (Tronco Vertical)",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.DIPS,
            primaryTarget = "Tríceps Braquial (Todas as Cabeças)",
            synergists = listOf("Deltoide Anterior", "Peitoral Inferior"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-0-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Mantenha o tronco vertical e cotovelos apontados diretamente para trás.",
                "Desça até 90° de flexão e estenda completamente no topo."
            ),
            biomechanicsTips = listOf(
                "Excelente construtor de massa geral no tríceps."
            ),
            commonMistakes = listOf(
                "Abrir os cotovelos para os lados."
            )
        ),
        Exercise(
            id = "tri_dip_machine",
            name = "Mergulho na Máquina (Triceps Dip Machine)",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.DIPS,
            primaryTarget = "Tríceps Braquial",
            synergists = listOf("Deltoide Anterior"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–14 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Costas apoiadas, empurre as manoplas até extensão completa dos cotovelos.",
                "Controle a volta até 90°."
            ),
            biomechanicsTips = listOf(
                "Alta estabilidade para treinar próximo à falha."
            ),
            commonMistakes = listOf(
                "Encolher os ombros durante a descida."
            )
        ),
        Exercise(
            id = "tri_skullcrusher_ez",
            name = "Tríceps Testa com Barra W",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.SKULLCRUSHER,
            primaryTarget = "Tríceps Braquial (Cabeças Lateral, Medial e Longa)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Deitado no banco, mantenha os braços inclinados ~15° para trás da vertical.",
                "Flexione apenas os cotovelos descendo a barra atrás da cabeça.",
                "Estenda mantendo os braços na mesma angulação."
            ),
            biomechanicsTips = listOf(
                "A leve inclinação mantém tensão constante mesmo no topo."
            ),
            commonMistakes = listOf(
                "Abrir demais os cotovelos."
            )
        ),
        Exercise(
            id = "tri_dumbbell_skullcrusher",
            name = "Tríceps Testa com Halteres (Pegada Neutra)",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.SKULLCRUSHER,
            primaryTarget = "Tríceps Braquial (Unilateral / Simétrico)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "10–14 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 75,
            executionSteps = listOf(
                "Segure os halteres em pegada neutra e desça ao lado das têmporas/orelhas.",
                "Estenda completamente contraindo o tríceps."
            ),
            biomechanicsTips = listOf(
                "Confortável para cotovelos sensíveis."
            ),
            commonMistakes = listOf(
                "Mover os ombros durante a repetição."
            )
        ),
        Exercise(
            id = "tri_overhead_cable_extension",
            name = "Tríceps Francês na Polia (Acima da Cabeça)",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.OVERHEAD_TRICEP,
            primaryTarget = "Tríceps Braquial (Ênfase Cabeça Longa)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "De costas para a polia, segure a corda acima da cabeça com cotovelos apontados para cima.",
                "Deixe as mãos descerem atrás da nuca alongando a cabeça longa.",
                "Estenda completamente à frente e acima."
            ),
            biomechanicsTips = listOf(
                "Posição overhead maximiza o crescimento da cabeça longa do tríceps."
            ),
            commonMistakes = listOf(
                "Arquear a lombar por falta de bracing."
            )
        ),
        Exercise(
            id = "tri_overhead_dumbbell_extension",
            name = "Tríceps Francês Unilateral com Halter",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.OVERHEAD_TRICEP,
            primaryTarget = "Tríceps Braquial (Cabeça Longa)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "10–14 reps / braço",
            tempoTUT = "3-1-1-0",
            restSeconds = 75,
            executionSteps = listOf(
                "Sentado, eleve o halter acima da cabeça com o braço vertical.",
                "Flexione o cotovelo descendo o halter atrás da cabeça.",
                "Estenda até o topo."
            ),
            biomechanicsTips = listOf(
                "Corrige desequilíbrios de força entre os braços."
            ),
            commonMistakes = listOf(
                "Deixar o cotovelo abrir lateralmente."
            )
        ),
        Exercise(
            id = "tri_cable_pushdown_rope",
            name = "Tríceps Pulley com Corda",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.TRICEP_PUSHDOWN,
            primaryTarget = "Tríceps Braquial (Cabeças Lateral e Medial)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Trave os cotovelos ao lado das costelas e estenda abrindo as pontas da corda no final.",
                "Contraia por 1 segundo e suba até 90°."
            ),
            biomechanicsTips = listOf(
                "Mantenha cotovelos imóveis como dobradiças."
            ),
            commonMistakes = listOf(
                "Debruçar-se sobre a corda usando o peso do tronco."
            )
        ),
        Exercise(
            id = "tri_cable_pushdown_bar",
            name = "Tríceps Pulley com Barra Reta / V",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.TRICEP_PUSHDOWN,
            primaryTarget = "Tríceps Braquial (Carga Progressiva)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Segure a barra V ou reta na altura do peito com cotovelos fixos.",
                "Empurre até extensão completa e controle a volta."
            ),
            biomechanicsTips = listOf(
                "Permite usar mais carga que a corda para tensão mecânica."
            ),
            commonMistakes = listOf(
                "Jogar os ombros para frente na descida."
            )
        ),
        Exercise(
            id = "quad_high_bar_squat",
            name = "Agachamento Livre High-Bar",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Quadríceps Femoral (Vastos e Reto)",
            synergists = listOf("Glúteo Máximo", "Adutor Magno", "Eretores", "Core"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "5–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 180,
            executionSteps = listOf(
                "Barra sobre trapézio superior, pés na largura dos ombros com pontas 20° para fora.",
                "Faça bracing abdominal e desça até quebra da paralela.",
                "Empurre o solo mantendo tronco ereto."
            ),
            biomechanicsTips = listOf(
                "Projeção natural dos joelhos sobre os pés maximiza o quadríceps."
            ),
            commonMistakes = listOf(
                "Valgo dinâmico (joelhos entrando na subida)."
            )
        ),
        Exercise(
            id = "quad_front_squat",
            name = "Agachamento Frontal com Barra (Front Squat)",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Quadríceps Femoral e Core Torácico",
            synergists = listOf("Glúteo Máximo", "Eretores Torácicos"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Avançado",
            recommendedReps = "6–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 180,
            executionSteps = listOf(
                "Apoie a barra sobre os deltoides anteriores com cotovelos altos.",
                "Desça profundamente mantendo o tronco quase vertical.",
                "Suba sem deixar os cotovelos caírem."
            ),
            biomechanicsTips = listOf(
                "Reduz cisalhamento lombar e isola intensamente o quadríceps."
            ),
            commonMistakes = listOf(
                "Deixar os cotovelos baixarem fazendo a barra rolar."
            )
        ),
        Exercise(
            id = "quad_hack_squat",
            name = "Agachamento Hack na Máquina",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Quadríceps Femoral (Ênfase Máxima)",
            synergists = listOf("Glúteo Máximo"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 150,
            executionSteps = listOf(
                "Costas firmes no encosto, pés na parte baixa da plataforma.",
                "Desça profundamente em 3 segundos, pause 1 segundo e suba."
            ),
            biomechanicsTips = listOf(
                "Um dos melhores construtores de quadríceps da musculação."
            ),
            commonMistakes = listOf(
                "Tirar os calcanhares da plataforma."
            )
        ),
        Exercise(
            id = "quad_smith_squat",
            name = "Agachamento no Smith (Pés à Frente)",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Quadríceps Femoral",
            synergists = listOf("Glúteo Máximo"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Posicione os pés 15–20 cm à frente da linha da barra.",
                "Desça mantendo o tronco vertical até flexão profunda dos joelhos."
            ),
            biomechanicsTips = listOf(
                "Guias lineares permitem focar 100% na flexão do joelho."
            ),
            commonMistakes = listOf(
                "Pés muito atrás da barra."
            )
        ),
        Exercise(
            id = "quad_leg_press_45",
            name = "Leg Press 45°",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.LEG_PRESS,
            primaryTarget = "Quadríceps Femoral",
            synergists = listOf("Glúteo Máximo", "Adutores"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "8–15 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 150,
            executionSteps = listOf(
                "Pés na região média-baixa da plataforma, segure nas alças travando o quadril.",
                "Desça até o máximo ângulo sem tirar o sacro do encosto.",
                "Empurre sem travar os joelhos no topo."
            ),
            biomechanicsTips = listOf(
                "Altíssimo volume sem fadiga axial na coluna."
            ),
            commonMistakes = listOf(
                "Deixar a lombar enrolar no final da descida."
            )
        ),
        Exercise(
            id = "quad_leg_press_horizontal",
            name = "Leg Press Horizontal",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.LEG_PRESS,
            primaryTarget = "Quadríceps Femoral",
            synergists = listOf("Glúteo Máximo"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-0-1-1",
            restSeconds = 120,
            executionSteps = listOf(
                "Ajuste o encosto para flexão profunda inicial de joelhos.",
                "Empurre até quase estender e controle a volta."
            ),
            biomechanicsTips = listOf(
                "Excelente opção segura para alta densidade e drop-sets."
            ),
            commonMistakes = listOf(
                "Hiperestender os joelhos no final."
            )
        ),
        Exercise(
            id = "quad_leg_extension",
            name = "Cadeira Extensora",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.LEG_EXTENSION,
            primaryTarget = "Quadríceps (Único que trabalha plenamente o Reto Femoral)",
            synergists = emptyList(),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–18 reps",
            tempoTUT = "2-0-1-2",
            restSeconds = 75,
            executionSteps = listOf(
                "Alinhe o eixo do joelho com o eixo da máquina.",
                "Estenda completamente e segure 2 segundos no topo.",
                "Desça até flexão total."
            ),
            biomechanicsTips = listOf(
                "Indispensável para hipertrofia completa do reto femoral."
            ),
            commonMistakes = listOf(
                "Dar chutes rápidos com impulso."
            )
        ),
        Exercise(
            id = "quad_bulgarian_split_squat",
            name = "Agachamento Búlgaro com Halteres",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.LUNGE_SPLIT,
            primaryTarget = "Quadríceps e Glúteo Máximo (Unilateral)",
            synergists = listOf("Glúteo Médio", "Adutores", "Core"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "8–12 reps / perna",
            tempoTUT = "3-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Peito do pé traseiro apoiado no banco, tronco ereto para focar quadríceps.",
                "Desça até o joelho de trás quase tocar o solo e empurre com a perna dianteira."
            ),
            biomechanicsTips = listOf(
                "Corrige assimetrias e gera estímulo extremo."
            ),
            commonMistakes = listOf(
                "Colocar os pés na mesma linha perdendo equilíbrio."
            )
        ),
        Exercise(
            id = "quad_walking_lunge",
            name = "Avanço / Passada Caminhando com Halteres",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.LUNGE_SPLIT,
            primaryTarget = "Quadríceps e Glúteos",
            synergists = listOf("Isquiotibiais", "Core"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "10–14 passos / perna",
            tempoTUT = "2-0-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Dê passos firmes descendo o joelho traseiro próximo ao chão.",
                "Impulsione através do meio do pé dianteiro."
            ),
            biomechanicsTips = listOf(
                "Passos médios enfatizam quadríceps; longos enfatizam glúteos."
            ),
            commonMistakes = listOf(
                "Bater o joelho no chão."
            )
        ),
        Exercise(
            id = "ham_rdl_barbell",
            name = "Stiff / Levantamento Terra Romeno (RDL)",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.RDL_HINGE,
            primaryTarget = "Isquiotibiais (Alongamento Pesado)",
            synergists = listOf("Glúteo Máximo", "Eretores da Espinha", "Antebraços"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "6–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 150,
            executionSteps = listOf(
                "Joelhos destravados (15° fixos), empurre o quadril para trás com a barra raspando nas pernas.",
                "Pare quando o quadril parar de recuar (meio da canela) e suba contraindo glúteos/posteriores."
            ),
            biomechanicsTips = listOf(
                "Principal exercício de sobrecarga excêntrica para posteriores."
            ),
            commonMistakes = listOf(
                "Afastar a barra das pernas.",
                "Arredondar a lombar."
            )
        ),
        Exercise(
            id = "ham_dumbbell_rdl",
            name = "RDL com Halteres (Bilateral / Unilateral)",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.RDL_HINGE,
            primaryTarget = "Isquiotibiais e Glúteo Máximo",
            synergists = listOf("Glúteo Médio", "Eretores"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Deslize os halteres ao lado das coxas e canelas empurrando o quadril para trás.",
                "Retorne sem hiperestender a lombar."
            ),
            biomechanicsTips = listOf(
                "A versão unilateral desafia equilíbrio e estabilidade pélvica."
            ),
            commonMistakes = listOf(
                "Dobrar demais os joelhos."
            )
        ),
        Exercise(
            id = "ham_seated_leg_curl",
            name = "Cadeira Flexora Sentada",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.LEG_CURL,
            primaryTarget = "Isquiotibiais (Posição Alongada no Quadril)",
            synergists = listOf("Gastrocnêmio"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Trave firme o apoio sobre as coxas e flexione puxando o rolo para baixo do banco.",
                "Controle a volta em 3 segundos."
            ),
            biomechanicsTips = listOf(
                "Estudos mostram hipertrofia superior à mesa flexora devido ao pré-alongamento no quadril."
            ),
            commonMistakes = listOf(
                "Deixar o apoio das coxas frouxo."
            )
        ),
        Exercise(
            id = "ham_lying_leg_curl",
            name = "Mesa Flexora Deitada",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.LEG_CURL,
            primaryTarget = "Isquiotibiais (Porção Curta do Bíceps Femoral)",
            synergists = listOf("Gastrocnêmio"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Deite-se pressionando o púbis contra o banco e flexione os joelhos.",
                "Contraia no topo por 1 segundo sem levantar o quadril."
            ),
            biomechanicsTips = listOf(
                "Mantenha dorsiflexão (ponta do pé puxada) para maior ativação."
            ),
            commonMistakes = listOf(
                "Levantar o quadril do banco."
            )
        ),
        Exercise(
            id = "ham_standing_leg_curl",
            name = "Flexora Unilateral em Pé na Máquina",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.LEG_CURL,
            primaryTarget = "Isquiotibiais (Isolamento Unilateral)",
            synergists = listOf("Gastrocnêmio"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps / perna",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Apoie o tronco e a coxa na almofada e flexione uma perna de cada vez.",
                "Controle a descida até extensão completa."
            ),
            biomechanicsTips = listOf(
                "Ótimo para corrigir assimetrias entre perna direita e esquerda."
            ),
            commonMistakes = listOf(
                "Impulsionar com a lombar."
            )
        ),
        Exercise(
            id = "ham_good_morning",
            name = "Bom-Dia (Good Morning) com Barra",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.RDL_HINGE,
            primaryTarget = "Isquiotibiais e Eretores da Espinha",
            synergists = listOf("Glúteo Máximo", "Core"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Avançado",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Barra apoiada no trapézio, joelhos semiflexionados, incline o tronco até próximo da paralela.",
                "Estenda o quadril retornando à vertical."
            ),
            biomechanicsTips = listOf(
                "Fortalece intensamente toda a cadeia posterior."
            ),
            commonMistakes = listOf(
                "Perder a neutralidade da coluna."
            )
        ),
        Exercise(
            id = "ham_nordic_curl",
            name = "Flexão Nórdica (Nordic Hamstring Curl)",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.LEG_CURL,
            primaryTarget = "Isquiotibiais (Força Excêntrica Extrema)",
            synergists = listOf("Glúteos", "Panturrilhas"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Avançado",
            recommendedReps = "5–8 reps",
            tempoTUT = "4-0-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Ajoelhado com tornozelos presos, desça o tronco à frente o mais devagar possível mantendo quadril reto.",
                "Amorteça com as mãos no chão e empurre suavemente para voltar."
            ),
            biomechanicsTips = listOf(
                "Reduz drasticamente o risco de estiramento de posteriores."
            ),
            commonMistakes = listOf(
                "Dobrar o quadril em vez de manter tronco e coxas alinhados."
            )
        ),
        Exercise(
            id = "glute_hip_thrust",
            name = "Elevação Pélvica com Barra (Hip Thrust)",
            muscleGroup = MuscleGroup.GLUTES,
            motionPattern = MotionPattern.HIP_THRUST,
            primaryTarget = "Glúteo Máximo (Pico de Contração Máximo)",
            synergists = listOf("Isquiotibiais", "Quadríceps", "Adutores"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 120,
            executionSteps = listOf(
                "Base das escápulas no banco, canelas verticais (90°) no topo.",
                "Empurre pelos calcanhares e faça retroversão pélvica no topo por 2 segundos."
            ),
            biomechanicsTips = listOf(
                "Mantenha o queixo apontado para o peito para não hiperestender a lombar."
            ),
            commonMistakes = listOf(
                "Jogar a cabeça para trás."
            )
        ),
        Exercise(
            id = "glute_single_leg_hip_thrust",
            name = "Elevação Pélvica Unilateral",
            muscleGroup = MuscleGroup.GLUTES,
            motionPattern = MotionPattern.HIP_THRUST,
            primaryTarget = "Glúteo Máximo e Glúteo Médio",
            synergists = listOf("Isquiotibiais", "Core"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "10–15 reps / perna",
            tempoTUT = "2-1-1-2",
            restSeconds = 75,
            executionSteps = listOf(
                "Apoie apenas um pé no solo e eleve o quadril até alinhamento completo.",
                "Segure 2 segundos no topo contraindo o glúteo."
            ),
            biomechanicsTips = listOf(
                "Altíssima ativação glútea mesmo sem cargas extremas."
            ),
            commonMistakes = listOf(
                "Deixar a pelve rodar para o lado."
            )
        ),
        Exercise(
            id = "glute_sumo_squat",
            name = "Agachamento Sumô com Barra / Halter",
            muscleGroup = MuscleGroup.GLUTES,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Glúteo Máximo e Adutores",
            synergists = listOf("Quadríceps", "Isquiotibiais"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Base bem aberta com pontas dos pés a 45° para fora.",
                "Desça empurrando os joelhos na direção dos pés e suba contraindo glúteos."
            ),
            biomechanicsTips = listOf(
                "Recruta fortemente os adutores e glúteo máximo."
            ),
            commonMistakes = listOf(
                "Deixar os joelhos caírem para dentro."
            )
        ),
        Exercise(
            id = "glute_cable_kickback",
            name = "Coice na Polia Baixa (Extensão de Quadril)",
            muscleGroup = MuscleGroup.GLUTES,
            motionPattern = MotionPattern.LUNGE_SPLIT,
            primaryTarget = "Glúteo Máximo (Fibras Superiores e Inferiores)",
            synergists = listOf("Isquiotibiais", "Glúteo Médio"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–16 reps / perna",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Tornozeleira na polia baixa, estenda o quadril para trás e ~15° para fora.",
                "Pare na contração máxima sem girar a pelve."
            ),
            biomechanicsTips = listOf(
                "A abdução de 15° acompanha as fibras do glúteo máximo."
            ),
            commonMistakes = listOf(
                "Compensar subindo a perna com a coluna lombar."
            )
        ),
        Exercise(
            id = "glute_abduction_machine",
            name = "Cadeira Abdutora",
            muscleGroup = MuscleGroup.GLUTES,
            motionPattern = MotionPattern.LUNGE_SPLIT,
            primaryTarget = "Glúteo Médio e Glúteo Máximo Superior",
            synergists = listOf("Tensor da Fáscia Lata"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "15–20 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Empurre os apoios externos abrindo as coxas ao máximo.",
                "Segure 1 segundo na abertura e controle a volta."
            ),
            biomechanicsTips = listOf(
                "Excelente para estética lateral do quadril e estabilidade pélvica."
            ),
            commonMistakes = listOf(
                "Balançar o tronco para abrir."
            )
        ),
        Exercise(
            id = "glute_cable_abduction",
            name = "Abdução de Quadril na Polia Baixa",
            muscleGroup = MuscleGroup.GLUTES,
            motionPattern = MotionPattern.LUNGE_SPLIT,
            primaryTarget = "Glúteo Médio (Estabilizador Pélvico)",
            synergists = listOf("Glúteo Mínimo"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "15–20 reps / perna",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Prenda a tornozeleira e abduza a perna lateralmente sem inclinar o tronco.",
                "Controle a descida cruzando levemente à frente."
            ),
            biomechanicsTips = listOf(
                "Tensão contínua do início ao fim da amplitude."
            ),
            commonMistakes = listOf(
                "Inclinar o corpo para o lado oposto."
            )
        ),
        Exercise(
            id = "calf_standing_machine",
            name = "Panturrilha em Pé na Máquina",
            muscleGroup = MuscleGroup.CALVES,
            motionPattern = MotionPattern.CALF_RAISE,
            primaryTarget = "Gastrocnêmio (Cabeças Medial e Lateral)",
            synergists = listOf("Sóleo"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps",
            tempoTUT = "2-2-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Joelhos estendidos, desça os calcanhares ao máximo e FAÇA PAUSA DE 2 SEGUNDOS no fundo.",
                "Suba na ponta dos pés contraindo por 1 segundo."
            ),
            biomechanicsTips = listOf(
                "A pausa de 2s elimina o efeito mola do tendão de Aquiles."
            ),
            commonMistakes = listOf(
                "Quicar rápido sem pausar embaixo."
            )
        ),
        Exercise(
            id = "calf_leg_press",
            name = "Panturrilha no Leg Press 45°",
            muscleGroup = MuscleGroup.CALVES,
            motionPattern = MotionPattern.CALF_RAISE,
            primaryTarget = "Gastrocnêmio",
            synergists = listOf("Sóleo"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–16 reps",
            tempoTUT = "2-2-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Apoie a ponta dos pés na borda inferior da plataforma com joelhos quase estendidos.",
                "Alongue 2 segundos no fundo e empurre com flexão plantar máxima."
            ),
            biomechanicsTips = listOf(
                "Permite carga progressiva alta sem pressão nos ombros."
            ),
            commonMistakes = listOf(
                "Dobrar os joelhos para empurrar a plataforma."
            )
        ),
        Exercise(
            id = "calf_seated_machine",
            name = "Panturrilha Sentado no Banco",
            muscleGroup = MuscleGroup.CALVES,
            motionPattern = MotionPattern.CALF_RAISE,
            primaryTarget = "Sóleo (Músculo Profundo da Panturrilha)",
            synergists = listOf("Gastrocnêmio"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–20 reps",
            tempoTUT = "2-2-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Joelhos a 90°, desça os calcanhares ao máximo com pausa de 2 segundos.",
                "Eleve até o pico de contração."
            ),
            biomechanicsTips = listOf(
                "Com joelhos flexionados, o sóleo realiza quase todo o trabalho."
            ),
            commonMistakes = listOf(
                "Amplitude curta."
            )
        ),
        Exercise(
            id = "calf_single_leg_dumbbell",
            name = "Panturrilha Unilateral com Halter em Degrau",
            muscleGroup = MuscleGroup.CALVES,
            motionPattern = MotionPattern.CALF_RAISE,
            primaryTarget = "Gastrocnêmio e Sóleo (Unilateral)",
            synergists = emptyList(),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps / perna",
            tempoTUT = "2-2-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Apoie um pé no degrau segurando um halter e use a mão livre para equilíbrio.",
                "Desça profundamente, pause 2s e suba."
            ),
            biomechanicsTips = listOf(
                "Corrige assimetrias entre panturrilhas."
            ),
            commonMistakes = listOf(
                "Fazer repetições rápidas sem controle."
            )
        ),
        Exercise(
            id = "core_cable_crunch",
            name = "Abdominal Supra na Polia Alta (Cable Crunch)",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.CRUNCH_FLEXION,
            primaryTarget = "Reto Abdominal (Sobrecarga Progressiva)",
            synergists = listOf("Oblíquos"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Ajoelhado frente à polia alta com corda ao lado das têmporas e quadril fixo.",
                "Enrole a coluna torácica levando os cotovelos em direção às coxas.",
                "Retorne alongando o abdômen sem sentar nos calcanhares."
            ),
            biomechanicsTips = listOf(
                "Permite progredir carga como qualquer outro grupo muscular."
            ),
            commonMistakes = listOf(
                "Dobrar o quadril sentando nos calcanhares."
            )
        ),
        Exercise(
            id = "core_hanging_leg_raise",
            name = "Elevação de Pernas Suspenso na Barra",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.LEG_RAISE,
            primaryTarget = "Reto Abdominal (Porção Infra) e Flexores",
            synergists = listOf("Oblíquos", "Antebraços"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "10–15 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Pendure-se na barra fixa e eleve as pernas ENROLANDO A PELVE para cima no final.",
                "Desça devagar sem balançar."
            ),
            biomechanicsTips = listOf(
                "A retroversão pélvica no topo ativa o reto abdominal."
            ),
            commonMistakes = listOf(
                "Balançar o corpo como pêndulo."
            )
        ),
        Exercise(
            id = "core_captains_chair",
            name = "Elevação de Joelhos na Paralela (Captain's Chair)",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.LEG_RAISE,
            primaryTarget = "Reto Abdominal Infra",
            synergists = listOf("Oblíquos"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–20 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Apoie os antebraços nas almofadas e eleve os joelhos em direção ao peito enrolando o quadril.",
                "Desça controladamente."
            ),
            biomechanicsTips = listOf(
                "Excelente opção sem limitar pela força da pegada."
            ),
            commonMistakes = listOf(
                "Parar os joelhos a 90° sem enrolar a pelve."
            )
        ),
        Exercise(
            id = "core_ab_wheel",
            name = "Roda Abdominal (Ab Wheel Rollout)",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.AB_WHEEL,
            primaryTarget = "Core Completo (Anti-Extensão Lombar)",
            synergists = listOf("Latíssimo do Dorso", "Serrátil Anterior"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "8–15 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 75,
            executionSteps = listOf(
                "Ajoelhado, contraia glúteos e abdômen e role à frente mantendo a lombar neutra.",
                "Retorne puxando com abdômen e dorsais."
            ),
            biomechanicsTips = listOf(
                "Ativação eletromiográfica máxima do reto abdominal e transverso."
            ),
            commonMistakes = listOf(
                "Deixar a barriga afundar arqueando a lombar."
            )
        ),
        Exercise(
            id = "core_weighted_plank",
            name = "Prancha Isométrica com Sobrecarga (RKC Plank)",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.AB_WHEEL,
            primaryTarget = "Transverso Abdominal e Estabilidade Profunda",
            synergists = listOf("Glúteos", "Serrátil"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "30–60 seg",
            tempoTUT = "Isometria",
            restSeconds = 60,
            executionSteps = listOf(
                "Apoie antebraços e pontas dos pés, faça retroversão pélvica e contraia glúteos e abdômen com força máxima.",
                "Adicione anilha nas costas conforme evoluir."
            ),
            biomechanicsTips = listOf(
                "Puxar isométricamente cotovelos em direção aos pés multiplica a ativação."
            ),
            commonMistakes = listOf(
                "Deixar o quadril subir ou afundar."
            )
        ),
        Exercise(
            id = "core_pallof_press",
            name = "Pallof Press na Polia (Anti-Rotação)",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.CRUNCH_FLEXION,
            primaryTarget = "Oblíquos Internos/Externos e Transverso",
            synergists = listOf("Glúteo Médio"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps / lado",
            tempoTUT = "2-2-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "De lado para a polia na altura do esterno, segure o pegador junto ao peito.",
                "Empurre os braços à frente resistindo à força lateral do cabo por 2 segundos."
            ),
            biomechanicsTips = listOf(
                "Blindagem funcional anti-rotacional para agachamento e levantamento terra."
            ),
            commonMistakes = listOf(
                "Deixar o tronco girar em direção à polia."
            )
        ),
        // --- EXPANDED CATALOG (+15 EXERCÍCIOS) ---
        Exercise(
            id = "chest_pullover_dumbbell",
            name = "Pullover com Halter no Banco Plano",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.FLY_CROSSOVER,
            primaryTarget = "Peitoral Maior (Porção Esternal) e Serrátil",
            synergists = listOf("Latíssimo do Dorso", "Tríceps (Cabeça Longa)"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Deite transversalmente no banco com apenas a parte superior das costas apoiada.",
                "Segure um halter com ambas as mãos acima do peito com cotovelos semiflexionados.",
                "Desça o halter em arco atrás da cabeça até alongamento profundo e retorne ao topo."
            ),
            biomechanicsTips = listOf("Mantenha o quadril ligeiramente abaixo da linha dos ombros para maximizar o alongamento."),
            commonMistakes = listOf("Dobrar excessivamente os cotovelos transformando o movimento em tríceps testa.")
        ),
        Exercise(
            id = "chest_smith_incline_press",
            name = "Supino Inclinado no Smith (30°)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.INCLINE_PRESS,
            primaryTarget = "Peitoral Maior (Porção Clavicular)",
            synergists = listOf("Deltoide Anterior", "Tríceps Braquial"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Posicione o banco a 30° alinhando a barra do Smith com a parte superior do tórax.",
                "Retraia as escápulas, destrave a barra e desça controladamente até próximo ao peito.",
                "Empurre até quase estender os cotovelos mantendo tensão constante."
            ),
            biomechanicsTips = listOf("Trajetória guiada permite isolar o peitoral superior sem gastar energia estabilizando."),
            commonMistakes = listOf("Deixar o banco muito inclinado (45–60°), recrutando excessivamente o ombro.")
        ),
        Exercise(
            id = "back_t_bar_row",
            name = "Remada T-Bar (Cavalinho Livre)",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Espessura Dorsal (Romboides, Trapézio Médio e Latíssimo)",
            synergists = listOf("Deltoide Posterior", "Eretores da Espinha", "Bíceps"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "6–10 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 150,
            executionSteps = listOf(
                "Encaixe a barra no suporte landmine e use pegador triângulo ou barra neutra.",
                "Incline o tronco a ~40° mantendo a coluna neutra e o abdômen travado.",
                "Puxe a carga em direção ao abdômen fechando as escápulas no topo."
            ),
            biomechanicsTips = listOf("Excelente sobrecarga mecânica para densidade do meio das costas."),
            commonMistakes = listOf("Arredondar a coluna lombar ou levantar o tronco a cada repetição.")
        ),
        Exercise(
            id = "back_wide_grip_seated_row",
            name = "Remada Baixa Sentada Pegada Aberta Pronada",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Trapézio Médio/Inferior, Romboides e Deltoide Posterior",
            synergists = listOf("Latíssimo do Dorso", "Bíceps Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–14 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Conecte a barra longa na polia baixa e segure com pegada pronada aberta.",
                "Puxe a barra em direção à base do esterno mantendo os cotovelos afastados a ~60°.",
                "Controle a volta permitindo que as escápulas se abram à frente."
            ),
            biomechanicsTips = listOf("Complementa perfeitamente as puxadas verticais construindo detalhamento escapular."),
            commonMistakes = listOf("Encolher os ombros em direção às orelhas durante a puxada.")
        ),
        Exercise(
            id = "back_meadows_row",
            name = "Remada Meadows Unilateral no Landmine",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Latíssimo do Dorso, Redondo Maior e Deltoide Posterior",
            synergists = listOf("Romboides", "Trapézio", "Antebraços"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "8–12 reps / lado",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Fique perpendicular à ponta da barra landmine e apoie o antebraço livre na coxa dianteira.",
                "Segure a extremidade da barra com pegada pronada (use straps se necessário).",
                "Puxe o cotovelo para cima e para trás alongando totalmente no fundo."
            ),
            biomechanicsTips = listOf("Ângulo único de tração que estira intensamente o latíssimo e redondo maior."),
            commonMistakes = listOf("Girar excessivamente o tronco para subir a barra.")
        ),
        Exercise(
            id = "shld_lateral_cable_behind_back",
            name = "Elevação Lateral na Polia Baixa por Trás do Corpo",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.LATERAL_RAISE,
            primaryTarget = "Deltoide Lateral (Tensão Máxima na Posição Alongada)",
            synergists = listOf("Supraespinal"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "12–16 reps / lado",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Posicione-se ao lado da polia baixa passando o cabo por trás das pernas.",
                "Abduza o braço lateralmente até a altura do ombro mantendo o cotovelo semiflexionado.",
                "Desça resistindo até a mão cruzar levemente atrás do quadril."
            ),
            biomechanicsTips = listOf("Alinha a resistência do cabo diretamente com as fibras acromiais do deltoide."),
            commonMistakes = listOf("Elevar o trapézio antes de iniciar a abdução do braço.")
        ),
        Exercise(
            id = "shld_upright_row_cable",
            name = "Remada Alta Aberta na Polia Baixa (Foco Deltoide Lateral)",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.LATERAL_RAISE,
            primaryTarget = "Deltoide Lateral e Trapézio Superior",
            synergists = listOf("Bíceps Braquial", "Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "2-0-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Segure a barra reta ou corda na polia baixa com pegada na largura dos ombros.",
                "Puxe liderando pelos cotovelos até a altura do peito/ombros (não passe disso).",
                "Desça controladamente mantendo tensão constante."
            ),
            biomechanicsTips = listOf("A pegada na largura dos ombros protege contra impacto subacromial."),
            commonMistakes = listOf("Usar pegada muito fechada e subir as mãos acima dos cotovelos.")
        ),
        Exercise(
            id = "bi_zottman_curl",
            name = "Rosca Zottman com Halteres",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Bíceps Braquial, Braquial e Braquiorradial",
            synergists = listOf("Extensores e Flexores do Antebraço"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "10–12 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Suba os halteres com pegada supinada (palmas para cima) contraindo o bíceps.",
                "No topo, gire os punhos 180° para pegada pronada (palmas para baixo).",
                "Desça lentamente em 3 segundos enfatizando o braquiorradial e antebraço."
            ),
            biomechanicsTips = listOf("Combina a força concêntrica da rosca direta com a sobrecarga excêntrica da rosca inversa."),
            commonMistakes = listOf("Girar os punhos antes de completar a subida.")
        ),
        Exercise(
            id = "bi_spider_curl",
            name = "Rosca Spider no Banco Inclinado (45°)",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.PREACHER_CURL,
            primaryTarget = "Bíceps Braquial (Pico de Contração)",
            synergists = listOf("Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 60,
            executionSteps = listOf(
                "Apoie o peito sobre o encosto do banco inclinado a 45° com os braços pendurados verticalmente.",
                "Flexione os cotovelos levando os halteres em direção aos ombros sem balançar os braços.",
                "Segure 2 segundos no topo de contração."
            ),
            biomechanicsTips = listOf("Impede totalmente qualquer impulso de tronco ou ombros."),
            commonMistakes = listOf("Puxar os cotovelos para trás durante a flexão.")
        ),
        Exercise(
            id = "tri_kickback_cable",
            name = "Tríceps Coice Unilateral na Polia Baixa",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.TRICEP_PUSHDOWN,
            primaryTarget = "Tríceps Braquial (Cabeça Lateral e Longa)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps / braço",
            tempoTUT = "2-1-1-2",
            restSeconds = 60,
            executionSteps = listOf(
                "Incline o tronco à frente segurando diretamente a esfera do cabo na polia baixa.",
                "Eleve o cotovelo até alinhar o braço paralelo ao tronco e estenda completamente o antebraço.",
                "Contraia 2 segundos no ponto máximo e retorne a 90°."
            ),
            biomechanicsTips = listOf("Muito superior à versão com halter por oferecer resistência contínua em todo o arco."),
            commonMistakes = listOf("Deixar o cotovelo cair a cada repetição.")
        ),
        Exercise(
            id = "tri_jm_press",
            name = "JM Press com Barra / Smith",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.SKULLCRUSHER,
            primaryTarget = "Tríceps Braquial (Força e Volume das 3 Cabeças)",
            synergists = listOf("Peitoral Maior", "Deltoide Anterior"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Avançado",
            recommendedReps = "6–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Deitado no banco plano com pegada fechada, desça a barra dobrando os cotovelos em direção ao queixo/pescoço.",
                "Pare quando os antebraços encostarem nos bíceps e empurre explosivamente estendendo os cotovelos."
            ),
            biomechanicsTips = listOf("Híbrido poderoso entre supino fechado e tríceps testa usado por powerlifters."),
            commonMistakes = listOf("Descer a barra no meio do peito como um supino comum.")
        ),
        Exercise(
            id = "quad_sissy_squat",
            name = "Agachamento Sissy (Sissy Squat)",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Quadríceps Femoral (Alongamento Extremo do Reto Femoral)",
            synergists = listOf("Core", "Flexores do Quadril"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Avançado",
            recommendedReps = "8–15 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Segure-se em um suporte fixo, fique na ponta dos pés e projete os joelhos à frente inclinando o tronco para trás.",
                "Mantenha quadril e tronco em linha reta enquanto desce os joelhos próximo ao chão.",
                "Empurre retornando à vertical."
            ),
            biomechanicsTips = listOf("Recruta o reto femoral em comprimento máximo como nenhum outro exercício livre."),
            commonMistakes = listOf("Dobrar a cintura para frente.")
        ),
        Exercise(
            id = "quad_goblet_squat",
            name = "Agachamento Goblet com Halter (Calcanhares Elevados)",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Quadríceps Femoral (Vasto Medial e Lateral)",
            synergists = listOf("Glúteo Máximo", "Core"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Apoie os calcanhares sobre anilhas baixas e segure um halter verticalmente junto ao peito.",
                "Agache profundamente mantendo o tronco totalmente ereto.",
                "Empurre o chão até estender os joelhos."
            ),
            biomechanicsTips = listOf("A elevação dos calcanhares aumenta a dorsiflexão e isola intensamente os quadríceps."),
            commonMistakes = listOf("Deixar os cotovelos baterem nos joelhos na descida.")
        ),
        Exercise(
            id = "ham_single_leg_rdl_dumbbell",
            name = "Stiff Unilateral com Halteres (Single-Leg RDL)",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.RDL_HINGE,
            primaryTarget = "Isquiotibiais e Glúteo Máximo/Médio",
            synergists = listOf("Eretores da Espinha", "Core"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "10–12 reps / perna",
            tempoTUT = "3-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Em apoio em uma perna com joelho destravado, incline o tronco à frente enquanto estende a perna livre atrás.",
                "Desça os halteres até a altura da canela mantendo o quadril nivelado.",
                "Suba contraindo o posterior e glúteo da perna de apoio."
            ),
            biomechanicsTips = listOf("Corrige assimetrias posteriores e fortalece a estabilidade pélvica."),
            commonMistakes = listOf("Abrir o quadril lateralmente durante a descida.")
        ),
        Exercise(
            id = "core_dragon_flag_negatives",
            name = "Elevação de Tronco / Dragon Flag Negativo",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.LEG_RAISE,
            primaryTarget = "Reto Abdominal Completo e Core Anti-Extensão",
            synergists = listOf("Latíssimo do Dorso", "Serrátil", "Glúteos"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Avançado",
            recommendedReps = "6–10 reps",
            tempoTUT = "4-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Deitado no banco plano, segure firmemente atrás da cabeça e eleve todo o corpo apoiado apenas nos ombros.",
                "Desça o corpo reto como uma prancha o mais devagar possível (4 segundos) sem encostar a lombar.",
                "Flexione os joelhos para subir novamente se necessário."
            ),
            biomechanicsTips = listOf("Um dos exercícios mais intensos de força abdominal da calistenia e musculação."),
            commonMistakes = listOf("Deixar a lombar arquear na descida.")
        )
    )

    fun getExerciseById(id: String): Exercise? = allExercises.find { it.id == id }

    val defaultSplits: List<WorkoutSplit> = listOf(
        WorkoutSplit(
            id = "split_ppl_pro",
            name = "PPL Pro — Push / Pull / Legs",
            subtitle = "6x na semana • Alta Frequência (2x/grupo)",
            frequencyPerWeek = 6,
            level = "Intermediário / Avançado",
            description = "Divisão padrão-ouro da hipertrofia moderna. Agrupa músculos sinérgicos (Empurrar, Puxar e Pernas) permitindo frequência 2x por semana com recuperação neural otimizada.",
            days = listOf(
                WorkoutDay(
                    id = "ppl_push_a",
                    dayLabel = "DIA A",
                    title = "Push A — Peito, Ombros & Tríceps",
                    focusSummary = "Tensão mecânica pesada nos empurrões",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("chest_bench_barbell", 4, "6–8", 2, 180),
                        PlannedExercise("chest_incline_dumbbell", 3, "8–12", 1, 120),
                        PlannedExercise("shld_overhead_press_dumbbell", 3, "8–12", 1, 120),
                        PlannedExercise("shld_lateral_raise_dumbbell", 4, "12–16", 1, 60),
                        PlannedExercise("tri_skullcrusher_ez", 3, "8–12", 1, 90),
                        PlannedExercise("tri_cable_pushdown_rope", 3, "12–15", 0, 75)
                    )
                ),
                WorkoutDay(
                    id = "ppl_pull_a",
                    dayLabel = "DIA B",
                    title = "Pull A — Costas, Posterior & Bíceps",
                    focusSummary = "Largura dorsal e espessura escapular",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("back_pull_up", 3, "6–10", 2, 120),
                        PlannedExercise("back_barbell_row", 4, "6–10", 2, 150),
                        PlannedExercise("back_seated_cable_row", 3, "10–12", 1, 90),
                        PlannedExercise("shld_reverse_pec_deck", 4, "12–16", 1, 60),
                        PlannedExercise("bi_barbell_ez_curl", 3, "8–12", 1, 75),
                        PlannedExercise("bi_hammer_curl", 3, "10–14", 0, 75)
                    )
                ),
                WorkoutDay(
                    id = "ppl_legs_a",
                    dayLabel = "DIA C",
                    title = "Legs A — Quadríceps & Panturrilhas",
                    focusSummary = "Construção de volume nos quadríceps",
                    estimatedMinutes = 70,
                    exercises = listOf(
                        PlannedExercise("quad_high_bar_squat", 4, "6–8", 2, 180),
                        PlannedExercise("quad_leg_press_45", 3, "10–12", 1, 150),
                        PlannedExercise("ham_seated_leg_curl", 3, "10–15", 1, 75),
                        PlannedExercise("quad_leg_extension", 3, "12–15", 0, 75),
                        PlannedExercise("calf_standing_machine", 4, "10–15", 1, 60),
                        PlannedExercise("core_cable_crunch", 3, "12–15", 1, 60)
                    )
                ),
                WorkoutDay(
                    id = "ppl_legs_b",
                    dayLabel = "DIA D",
                    title = "Legs B — Posteriores, Glúteos & Core",
                    focusSummary = "Cadeia posterior pesada e estabilidade",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("ham_rdl_barbell", 4, "6–10", 2, 150),
                        PlannedExercise("glute_hip_thrust", 4, "8–12", 1, 120),
                        PlannedExercise("quad_bulgarian_split_squat", 3, "8–12", 1, 90),
                        PlannedExercise("ham_lying_leg_curl", 3, "12–15", 0, 75),
                        PlannedExercise("calf_seated_machine", 4, "12–20", 1, 60),
                        PlannedExercise("core_hanging_leg_raise", 3, "10–15", 1, 60)
                    )
                )
            )
        ),
        WorkoutSplit(
            id = "split_upper_lower",
            name = "Upper / Lower — Ciência & Performance",
            subtitle = "4x na semana • Recuperação Otimizada",
            frequencyPerWeek = 4,
            level = "Todos os Níveis",
            description = "Excelente para atletas naturais que buscam progressão consistente de carga sem fadiga sistêmica excessiva.",
            days = listOf(
                WorkoutDay(
                    id = "ul_upper_1",
                    dayLabel = "UPPER 1",
                    title = "Superiores A — Ênfase Horizontal",
                    focusSummary = "Supinos, remadas e braços",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("chest_bench_barbell", 4, "6–8", 2, 180),
                        PlannedExercise("back_barbell_row", 4, "6–10", 2, 150),
                        PlannedExercise("chest_incline_dumbbell", 3, "10–12", 1, 90),
                        PlannedExercise("back_lat_pulldown", 3, "10–12", 1, 90),
                        PlannedExercise("shld_cable_lateral_raise", 4, "12–16", 1, 60),
                        PlannedExercise("bi_incline_dumbbell_curl", 3, "10–12", 1, 75),
                        PlannedExercise("tri_overhead_cable_extension", 3, "10–14", 1, 75)
                    )
                ),
                WorkoutDay(
                    id = "ul_lower_1",
                    dayLabel = "LOWER 1",
                    title = "Inferiores A — Agachamento & Quadríceps",
                    focusSummary = "Força de membros inferiores",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("quad_high_bar_squat", 4, "5–8", 2, 180),
                        PlannedExercise("ham_rdl_barbell", 3, "8–10", 2, 150),
                        PlannedExercise("quad_hack_squat", 3, "10–12", 1, 120),
                        PlannedExercise("ham_seated_leg_curl", 3, "12–15", 1, 75),
                        PlannedExercise("calf_standing_machine", 4, "10–15", 1, 60),
                        PlannedExercise("core_ab_wheel", 3, "10–15", 1, 60)
                    )
                ),
                WorkoutDay(
                    id = "ul_upper_2",
                    dayLabel = "UPPER 2",
                    title = "Superiores B — Vertical & Ombros",
                    focusSummary = "Desenvolvimento, puxadas e isolamento",
                    estimatedMinutes = 60,
                    exercises = listOf(
                        PlannedExercise("shld_overhead_press_dumbbell", 4, "8–10", 2, 120),
                        PlannedExercise("back_pull_up", 4, "6–10", 1, 120),
                        PlannedExercise("chest_pec_deck", 3, "12–15", 1, 75),
                        PlannedExercise("back_chest_supported_row", 3, "10–12", 1, 90),
                        PlannedExercise("shld_face_pull", 3, "15–20", 1, 60),
                        PlannedExercise("bi_preacher_curl", 3, "12–15", 0, 60),
                        PlannedExercise("tri_cable_pushdown_rope", 3, "12–15", 0, 60)
                    )
                ),
                WorkoutDay(
                    id = "ul_lower_2",
                    dayLabel = "LOWER 2",
                    title = "Inferiores B — Posterior & Glúteo",
                    focusSummary = "Cadeia posterior e unilateral",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("glute_hip_thrust", 4, "8–12", 1, 120),
                        PlannedExercise("quad_leg_press_45", 4, "10–14", 1, 120),
                        PlannedExercise("ham_lying_leg_curl", 3, "10–15", 1, 75),
                        PlannedExercise("quad_leg_extension", 3, "12–15", 0, 75),
                        PlannedExercise("calf_seated_machine", 4, "12–20", 1, 60),
                        PlannedExercise("core_cable_crunch", 3, "12–15", 1, 60)
                    )
                )
            )
        ),
        WorkoutSplit(
            id = "split_abcde_classic",
            name = "ABCDE — Bodybuilding Clássico",
            subtitle = "5x na semana • Especialização Muscular",
            frequencyPerWeek = 5,
            level = "Intermediário / Avançado",
            description = "Permite alto volume concentrado por sessão e conexão mente-músculo profunda em cada grupamento muscular.",
            days = listOf(
                WorkoutDay(
                    id = "abcde_a",
                    dayLabel = "TREINO A",
                    title = "Peitoral Completo & Abdômen",
                    focusSummary = "Porções clavicular, esternal e costal",
                    estimatedMinutes = 60,
                    exercises = listOf(
                        PlannedExercise("chest_incline_dumbbell", 4, "8–12", 1, 120),
                        PlannedExercise("chest_bench_barbell", 4, "6–10", 2, 150),
                        PlannedExercise("chest_incline_machine", 3, "10–12", 1, 90),
                        PlannedExercise("chest_cable_crossover_high", 3, "12–15", 0, 75),
                        PlannedExercise("chest_pec_deck", 3, "12–15", 0, 75),
                        PlannedExercise("core_cable_crunch", 4, "12–15", 1, 60)
                    )
                ),
                WorkoutDay(
                    id = "abcde_b",
                    dayLabel = "TREINO B",
                    title = "Costas & Trapézio",
                    focusSummary = "Largura e densidade dorsal",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("back_lat_pulldown", 4, "8–12", 1, 90),
                        PlannedExercise("back_barbell_row", 4, "6–10", 2, 150),
                        PlannedExercise("back_chest_supported_row", 3, "10–12", 1, 90),
                        PlannedExercise("back_seated_cable_row", 3, "10–12", 1, 90),
                        PlannedExercise("back_straight_arm_pulldown", 3, "12–15", 0, 60)
                    )
                ),
                WorkoutDay(
                    id = "abcde_c",
                    dayLabel = "TREINO C",
                    title = "Pernas Completas (Legs)",
                    focusSummary = "Quadríceps, posteriores, glúteos e panturrilha",
                    estimatedMinutes = 75,
                    exercises = listOf(
                        PlannedExercise("quad_high_bar_squat", 4, "6–10", 2, 180),
                        PlannedExercise("quad_leg_press_45", 3, "10–12", 1, 120),
                        PlannedExercise("ham_rdl_barbell", 3, "8–10", 2, 150),
                        PlannedExercise("ham_seated_leg_curl", 3, "12–15", 1, 75),
                        PlannedExercise("quad_leg_extension", 3, "12–15", 0, 75),
                        PlannedExercise("calf_standing_machine", 4, "12–15", 1, 60)
                    )
                ),
                WorkoutDay(
                    id = "abcde_d",
                    dayLabel = "TREINO D",
                    title = "Ombros 3D (Anterior, Lateral & Posterior)",
                    focusSummary = "Proporção e largura de ombros",
                    estimatedMinutes = 55,
                    exercises = listOf(
                        PlannedExercise("shld_overhead_press_dumbbell", 4, "8–12", 1, 120),
                        PlannedExercise("shld_lateral_raise_dumbbell", 4, "12–16", 1, 60),
                        PlannedExercise("shld_cable_lateral_raise", 3, "12–16", 0, 60),
                        PlannedExercise("shld_reverse_pec_deck", 4, "12–16", 1, 60),
                        PlannedExercise("shld_face_pull", 3, "15–20", 1, 60)
                    )
                ),
                WorkoutDay(
                    id = "abcde_e",
                    dayLabel = "TREINO E",
                    title = "Braços Completos (Bíceps & Tríceps)",
                    focusSummary = "Super-bombeamento e tensão mecânica",
                    estimatedMinutes = 55,
                    exercises = listOf(
                        PlannedExercise("tri_close_grip_bench", 3, "6–10", 2, 120),
                        PlannedExercise("bi_barbell_ez_curl", 3, "8–12", 1, 75),
                        PlannedExercise("tri_overhead_cable_extension", 3, "10–14", 1, 75),
                        PlannedExercise("bi_incline_dumbbell_curl", 3, "10–14", 1, 75),
                        PlannedExercise("tri_cable_pushdown_rope", 3, "12–15", 0, 60),
                        PlannedExercise("bi_hammer_curl", 3, "10–14", 0, 60)
                    )
                )
            )
        ),
        WorkoutSplit(
            id = "split_arnold",
            name = "Arnold Split — Peito/Costas • Ombros/Braços • Pernas",
            subtitle = "6x na semana • Antagonistas & Super-Bomba",
            frequencyPerWeek = 6,
            level = "Avançado",
            description = "Divisão clássica de alta densidade que combina músculos antagonistas (Peito + Costas e Bíceps + Tríceps + Ombros) permitindo recuperação recíproca entre séries.",
            days = listOf(
                WorkoutDay(
                    id = "arn_1",
                    dayLabel = "DIA 1",
                    title = "Peito & Costas (Antagonistas)",
                    focusSummary = "Volume denso de tronco superior",
                    estimatedMinutes = 70,
                    exercises = listOf(
                        PlannedExercise("chest_incline_barbell", 4, "6–10", 2, 150),
                        PlannedExercise("back_pull_up", 4, "6–10", 2, 120),
                        PlannedExercise("chest_bench_dumbbell", 3, "8–12", 1, 120),
                        PlannedExercise("back_one_arm_dumbbell_row", 3, "8–12", 1, 90),
                        PlannedExercise("chest_pec_deck", 3, "12–15", 0, 60),
                        PlannedExercise("back_straight_arm_pulldown", 3, "12–15", 0, 60)
                    )
                ),
                WorkoutDay(
                    id = "arn_2",
                    dayLabel = "DIA 2",
                    title = "Ombros, Bíceps & Tríceps",
                    focusSummary = "Deltoides 3D e braços completos",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("shld_barbell_ohp", 4, "6–8", 2, 150),
                        PlannedExercise("shld_lateral_raise_dumbbell", 4, "12–18", 1, 60),
                        PlannedExercise("bi_straight_bar_curl", 3, "8–12", 1, 75),
                        PlannedExercise("tri_skullcrusher_ez", 3, "8–12", 1, 75),
                        PlannedExercise("bi_bayesian_cable_curl", 3, "12–15", 0, 60),
                        PlannedExercise("tri_overhead_cable_extension", 3, "12–15", 0, 60)
                    )
                ),
                WorkoutDay(
                    id = "arn_3",
                    dayLabel = "DIA 3",
                    title = "Pernas & Core",
                    focusSummary = "Agachamento, posterior e panturrilha",
                    estimatedMinutes = 70,
                    exercises = listOf(
                        PlannedExercise("quad_high_bar_squat", 4, "6–8", 2, 180),
                        PlannedExercise("ham_rdl_barbell", 4, "6–10", 2, 150),
                        PlannedExercise("quad_leg_extension", 3, "12–15", 0, 75),
                        PlannedExercise("ham_seated_leg_curl", 3, "12–15", 0, 75),
                        PlannedExercise("calf_standing_machine", 4, "10–15", 1, 60),
                        PlannedExercise("core_ab_wheel", 3, "10–15", 1, 60)
                    )
                )
            )
        ),
        WorkoutSplit(
            id = "split_fullbody_3x",
            name = "Full Body — Força Base & Eficiência",
            subtitle = "3x na semana • Síntese Proteica Contínua",
            frequencyPerWeek = 3,
            level = "Iniciante / Intermediário",
            description = "Estimula todo o corpo 3x na semana com 4 dias livres para máxima recuperação.",
            days = listOf(
                WorkoutDay(
                    id = "fb_a",
                    dayLabel = "FULL BODY A",
                    title = "Sessão A — Agachamento & Supino",
                    focusSummary = "Compostos fundamentais",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("quad_high_bar_squat", 4, "6–8", 2, 180),
                        PlannedExercise("chest_bench_barbell", 4, "6–8", 2, 150),
                        PlannedExercise("back_barbell_row", 3, "8–10", 2, 120),
                        PlannedExercise("shld_lateral_raise_dumbbell", 3, "12–16", 1, 60),
                        PlannedExercise("bi_barbell_ez_curl", 3, "10–12", 1, 60),
                        PlannedExercise("calf_standing_machine", 3, "12–15", 1, 60)
                    )
                ),
                WorkoutDay(
                    id = "fb_b",
                    dayLabel = "FULL BODY B",
                    title = "Sessão B — Terra Romeno & Desenvolvimento",
                    focusSummary = "Cadeia posterior e empurrão vertical",
                    estimatedMinutes = 65,
                    exercises = listOf(
                        PlannedExercise("ham_rdl_barbell", 4, "6–10", 2, 150),
                        PlannedExercise("shld_overhead_press_dumbbell", 4, "8–10", 2, 120),
                        PlannedExercise("back_lat_pulldown", 3, "10–12", 1, 90),
                        PlannedExercise("quad_leg_press_45", 3, "10–12", 1, 120),
                        PlannedExercise("tri_cable_pushdown_rope", 3, "12–15", 1, 60),
                        PlannedExercise("core_cable_crunch", 3, "12–15", 1, 60)
                    )
                )
            )
        )
    )

    val scienceArticles: List<ScienceArticle> = listOf(
        ScienceArticle(
            id = "sci_progressive_overload",
            category = "Princípio Fundamental",
            title = "Sobrecarga Progressiva: O Motor Real da Hipertrofia",
            readTimeMinutes = 5,
            summary = "Sem aumento gradual de tensão mecânica ao longo das semanas, o músculo não tem motivo biológico para sintetizar novas proteínas contráteis.",
            keyTakeaways = listOf(
                "Dupla Progressão: Escolha uma faixa (ex.: 8–12 reps). Quando atingir 12 reps limpas em todas as séries, aumente +2 kg a +5 kg e volte para 8 reps.",
                "Progressão de Execução: Fazer o mesmo peso com controle excêntrico de 3s e amplitude total é sobrecarga real.",
                "Registro Obrigatório: Nunca treine no 'achismo'; bata o número da semana anterior em pelo menos 1 rep ou 1 kg."
            ),
            detailedProtocol = "A hipertrofia miofibrilar é governada primariamente pela Tensão Mecânica. Para progredir por anos sem estagnar: (1) Trabalhe dentro de faixas de repetição definidas; (2) Suba carga apenas quando dominar o teto da faixa sem degradar a técnica; (3) A cada 6–8 semanas de progressão dura, aplique 1 semana de Deload."
        ),
        ScienceArticle(
            id = "sci_volume_landmarks",
            category = "Programação de Treino",
            title = "Marcos de Volume Semanal: MEV, MAV e MRV",
            readTimeMinutes = 6,
            summary = "Entenda quantas séries válidas por semana cada músculo precisa para crescer sem entrar em overreaching não-funcional.",
            keyTakeaways = listOf(
                "MV (Volume de Manutenção): ~4–6 séries/semana mantêm a massa muscular já conquistada.",
                "MEV (Volume Mínimo Efetivo): ~6–10 séries/semana iniciam hipertrofia mensurável.",
                "MAV (Volume Máximo Adaptativo): ~12–20 séries/semana — onde ocorre a maior taxa de ganho muscular.",
                "MRV (Volume Máximo Recuperável): Acima de 20–25 séries/semana a fadiga supera a recuperação."
            ),
            detailedProtocol = "Inicie um mesociclo próximo ao MEV e adicione 1 a 2 séries por semana conforme sua recuperação permitir até atingir o MAV/MRV na semana 5 ou 6."
        ),
        ScienceArticle(
            id = "sci_rir_failure",
            category = "Intensidade & Esforço",
            title = "RIR (Repetições na Reserva) vs. Falha Concêntrica",
            readTimeMinutes = 5,
            summary = "Ir até a falha absoluta em todas as séries gera fadiga neural exponencial sem ganho proporcional de hipertrofia.",
            keyTakeaways = listOf(
                "RIR 2 (RPE 8): Você encerra a série sabendo que faria no máximo mais 2 repetições limpas.",
                "Compostos Pesados: Mantenha em RIR 1–3 para preservar o SNC e a técnica.",
                "Isoladores e Máquinas: Pode levar a RIR 0–1 com segurança."
            ),
            detailedProtocol = "Pesquisas recentes demonstram que séries encerradas com 1 a 2 repetições na reserva (RIR 1–2) produzem praticamente a mesma hipertrofia que a falha total (RIR 0), porém com recuperação muscular 24 a 48 horas mais rápida."
        ),
        ScienceArticle(
            id = "sci_nutrition_macros",
            category = "Nutrição Esportiva",
            title = "Engenharia de Macronutrientes: Bulking Limpo vs. Cutting",
            readTimeMinutes = 6,
            summary = "Como calibrar calorias, proteínas, carboidratos e gorduras para maximizar síntese proteica e minimizar ganho de gordura.",
            keyTakeaways = listOf(
                "Proteína: 1,8 g a 2,2 g/kg de peso corporal (até 2,4 g/kg em cutting agressivo).",
                "Gorduras Essenciais: 0,7 g a 1,0 g/kg para sustentar produção endógena hormonal.",
                "Carboidratos: Combustível primário do glicogênio muscular e performance anaeróbica."
            ),
            detailedProtocol = "Para Bulking Limpo: aplique superávit de +250 a +400 kcal acima do GET. Para Cutting: déficit de -400 a -600 kcal mantendo as cargas altas no treino."
        ),
        ScienceArticle(
            id = "sci_supplements",
            category = "Suplementação Baseada em Ciência",
            title = "O Que Realmente Funciona (Nível A de Evidência)",
            readTimeMinutes = 4,
            summary = "Separe suplementos comprovados cientificamente do marketing vazio da indústria fitness.",
            keyTakeaways = listOf(
                "Creatina Monohidratada (3–5 g/dia): Aumenta ressíntese de ATP, força e volume celular.",
                "Whey Protein / Albumina / Soja: Praticidade para bater a meta diária de leucina.",
                "Cafeína Anidra (3–6 mg/kg 45 min pré-treino): Reduz percepção de esforço.",
                "Citrulina Malato (6–8 g pré-treino): Melhora óxido nítrico e resistência à fadiga."
            ),
            detailedProtocol = "A creatina funciona por saturação crônica — tome todos os dias, inclusive nos dias sem treino."
        ),
        ScienceArticle(
            id = "sci_periodization_deload",
            category = "Longevidade & Performance",
            title = "Periodização & Deload Estratégico",
            readTimeMinutes = 5,
            summary = "O músculo cresce no descanso, não durante a sessão. Aprenda a dissipar a fadiga acumulada para quebrar platôs.",
            keyTakeaways = listOf(
                "Protocolo de Deload (7 dias): Reduza o número de séries em 40–50% e cargas em 10%.",
                "Supercompensação: Na semana pós-deload, é comum bater novos recordes pessoais (PRs)."
            ),
            detailedProtocol = "Estruture seus treinos em blocos de 5 a 7 semanas de acumulação progressiva seguidos por 1 semana de Deload."
        )
    )
}
