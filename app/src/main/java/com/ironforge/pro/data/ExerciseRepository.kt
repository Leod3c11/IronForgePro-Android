package com.ironforge.pro.data

import com.ironforge.pro.model.*

object ExerciseRepository {

    val allExercises: List<Exercise> = listOf(
        // ==================== PEITORAL (CHEST) ====================
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
                "Retraia e deprima as escápulas formando um arco torácico natural e estável.",
                "Segure a barra ligeiramente além da largura dos ombros e retire-a do suporte.",
                "Desça a barra controladamente até tocar a linha média/inferior do esterno, com cotovelos a ~45-60° do tronco, e empurre explosivamente."
            ),
            biomechanicsTips = listOf(
                "Mantenha os punhos neutros alinhados aos antebraços para evitar torque articular.",
                "Use leg drive empurrando o chão com os pés para estabilizar o tronco durante a fase concêntrica."
            ),
            commonMistakes = listOf(
                "Abrir os cotovelos a 90° (guilhotina), sobrecarregando o manguito rotador.",
                "Quicar a barra no tórax ou tirar os glúteos do banco."
            )
        ),
        Exercise(
            id = "chest_incline_dumbbell",
            name = "Supino Inclinado com Halteres (30°)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.INCLINE_PRESS,
            primaryTarget = "Peitoral Maior (Porção Clavicular / Superior)",
            synergists = listOf("Deltoide Anterior", "Tríceps Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Ajuste o banco em inclinação de 30° (evite 45°+ para não transferir excessivamente para o deltoide anterior).",
                "Apoie os halteres nas coxas e impulsione-os para a posição inicial com escápulas retraídas.",
                "Desça os halteres ao lado da porção superior do peitoral buscando alongamento profundo.",
                "Empurre convergindo suavemente os halteres no topo sem bater um no outro."
            ),
            biomechanicsTips = listOf(
                "A amplitude livre dos halteres permite maior estiramento sob carga na porção clavicular.",
                "Mantenha o peito estufado durante todo o movimento."
            ),
            commonMistakes = listOf(
                "Usar inclinação excessiva (45° ou 60°), transformando o exercício em desenvolvimento.",
                "Encurtar a fase excêntrica descendo apenas até a metade."
            )
        ),
        Exercise(
            id = "chest_cable_crossover_high",
            name = "Crucifixo na Polia Alta (Crossover)",
            muscleGroup = MuscleGroup.CHEST,
            motionPattern = MotionPattern.FLY_CROSSOVER,
            primaryTarget = "Peitoral Maior (Fibras Inferiores e Esternais)",
            synergists = listOf("Deltoide Anterior", "Bíceps Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 75,
            executionSteps = listOf(
                "Posicione as polias acima da linha da cabeça e dê um passo à frente em base alternada.",
                "Mantenha uma leve flexão fixa nos cotovelos (15–20°) durante todo o arco.",
                "Traga os pegadores para baixo e para frente até cruzar levemente os punhos na linha do umbigo.",
                "Segure 1–2 segundos no pico de contração e retorne controladamente."
            ),
            biomechanicsTips = listOf(
                "Cabos oferecem tensão contínua mesmo no ponto de contração máxima, onde halteres perdem resistência.",
                "Pense em aproximar os bíceps um do outro, não apenas as mãos."
            ),
            commonMistakes = listOf(
                "Flexionar e estender os cotovelos transformando o exercício em supino na polia.",
                "Inclinar excessivamente o tronco para compensar carga alta."
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
                "Ajuste o banco para que as manoplas fiquem alinhadas exatamente com o meio do peitoral.",
                "Mantenha as escápulas apoiadas no encosto e os ombros baixos longe das orelhas.",
                "Feche os braços até unir as manoplas à frente, contraindo intensamente o peitoral.",
                "Controle a volta por 3 segundos sem deixar os pesos baterem."
            ),
            biomechanicsTips = listOf(
                "Excelente opção para levar o peitoral próximo à falha mecânica com total segurança.",
                "Evite projetar os ombros para frente no final da concêntrica."
            ),
            commonMistakes = listOf(
                "Banco muito baixo fazendo os cotovelos subirem e estressando o ombro.",
                "Soltar o peso rapidamente na volta."
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
                "Segure as barras paralelas com pegada ligeiramente aberta e incline o tronco ~30° à frente.",
                "Mantenha as pernas levemente dobradas atrás e o queixo próximo ao peito.",
                "Desça até que os ombros fiquem ligeiramente abaixo da linha dos cotovelos.",
                "Empurre até quase estender os cotovelos mantendo a inclinação do tronco."
            ),
            biomechanicsTips = listOf(
                "A inclinação anterior do tronco aumenta o braço de momento para o peitoral inferior.",
                "Adicione carga com cinto de lastro quando ultrapassar 12 repetições limpas."
            ),
            commonMistakes = listOf(
                "Manter o tronco totalmente vertical (foca quase exclusivamente no tríceps).",
                "Descer além da mobilidade individual do ombro."
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
                "Ajuste o assento para alinhar as manoplas com a porção superior do peito.",
                "Retraia as escápulas e empurre até quase travar os cotovelos.",
                "Controle a excêntrica buscando máximo alongamento."
            ),
            biomechanicsTips = listOf(
                "Trajetória convergente fixa maximiza a tensão no peitoral superior com alta segurança."
            ),
            commonMistakes = listOf(
                "Descolar os ombros do encosto no final do empurrão."
            )
        ),

        // ==================== COSTAS & DORSAIS (BACK) ====================
        Exercise(
            id = "back_lat_pulldown",
            name = "Puxada Frontal Pegada Pronada",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.LAT_PULLDOWN,
            primaryTarget = "Latíssimo do Dorso (Largura Dorsal)",
            synergists = listOf("Redondo Maior", "Romboides", "Trapézio Inferior", "Bíceps Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "8–12 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Trave as coxas sob o apoio e segure a barra a cerca de 1,3x a largura dos ombros.",
                "Inicie o movimento deprimindo os ombros (abaixando as escápulas) antes de dobrar os cotovelos.",
                "Puxe a barra em direção à parte superior do esterno inclinando o tronco apenas 10–15°.",
                "Estenda completamente os braços no topo permitindo o alongamento total das dorsais."
            ),
            biomechanicsTips = listOf(
                "Use pegada sem polegar (thumbless) ou straps se o antebraço falhar antes das dorsais.",
                "Concentre-se em puxar com os cotovelos para baixo e para os quadris."
            ),
            commonMistakes = listOf(
                "Balançar o tronco para trás usando impulso lombar.",
                "Puxar a barra atrás da nuca (alto risco de impacto cervical e glenoumeral)."
            )
        ),
        Exercise(
            id = "back_barbell_row",
            name = "Remada Curvada com Barra",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Latíssimo do Dorso, Romboides e Trapézio Médio",
            synergists = listOf("Deltoide Posterior", "Eretores da Espinha", "Bíceps Braquial"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "6–10 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 150,
            executionSteps = listOf(
                "Com pés na largura dos quadris, flexione o quadril mantendo a coluna neutra em 35–45°.",
                "Segure a barra com pegada pronada ou supinada na largura dos ombros.",
                "Puxe a barra em direção à linha do umbigo fechando as escápulas no topo.",
                "Controle a descida sem arredondar a região lombar."
            ),
            biomechanicsTips = listOf(
                "Puxar mais baixo enfatiza o latíssimo; puxar mais alto enfatiza trapézio médio e romboides.",
                "Mantenha o bracing abdominal ativo em 100% da série."
            ),
            commonMistakes = listOf(
                "Arredondar a lombar sob fadiga.",
                "Ficar quase em pé transformando o movimento em encolhimento de trapézio."
            )
        ),
        Exercise(
            id = "back_seated_cable_row",
            name = "Remada Baixa Sentada na Polia",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.ROW_HORIZONTAL,
            primaryTarget = "Espessura Dorsal (Romboides, Trapézio Médio e Latíssimo)",
            synergists = listOf("Deltoide Posterior", "Bíceps Braquial", "Braquial"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante",
            recommendedReps = "10–12 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 90,
            executionSteps = listOf(
                "Apoie os pés na plataforma com joelhos semiflexionados e coluna ereta.",
                "Permita que as escápulas abram à frente no início para alongar o meio das costas.",
                "Puxe o triângulo até tocar o abdômen enquanto retrai e deprime as escápulas.",
                "Segure 1 segundo na contração máxima antes de retornar."
            ),
            biomechanicsTips = listOf(
                "Mantenha os cotovelos raspando próximos às costelas para maximizar o recrutamento dorsal.",
                "Evite elevar os ombros em direção às orelhas."
            ),
            commonMistakes = listOf(
                "Usar gangorra excessiva de tronco para frente e para trás.",
                "Encurtar o alongamento escapular na fase excêntrica."
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
                "Segure a barra fixa acima da cabeça com pegada pronada aberta.",
                "Parta da posição de suspensão ativa (ombros longe das orelhas).",
                "Puxe o peito em direção à barra até passar o queixo acima da linha da barra.",
                "Desça lentamente até a extensão completa dos cotovelos."
            ),
            biomechanicsTips = listOf(
                "Imagine levar os cotovelos até os bolsos da calça para ativar intensamente o latíssimo.",
                "Se ainda não completa 6 reps, utilize elástico de assistência ou graviton."
            ),
            commonMistakes = listOf(
                "Fazer meias repetições sem estender os braços embaixo.",
                "Chutar as pernas (kipping) durante treino de hipertrofia."
            )
        ),
        Exercise(
            id = "back_straight_arm_pulldown",
            name = "Pulldown Braços Estendidos na Polia",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.STRAIGHT_ARM_PULL,
            primaryTarget = "Latíssimo do Dorso (Isolamento)",
            synergists = listOf("Redondo Maior", "Tríceps (Cabeça Longa)", "Peitoral Menor"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–15 reps",
            tempoTUT = "2-1-1-2",
            restSeconds = 60,
            executionSteps = listOf(
                "Em pé frente à polia alta com barra reta ou corda, incline o tronco ~25° à frente.",
                "Com cotovelos travados em leve flexão, puxe a barra em arco até encostar nas coxas.",
                "Contraia fortemente as dorsais por 1–2 segundos.",
                "Retorne até os braços ficarem alinhados com o tronco acima da cabeça."
            ),
            biomechanicsTips = listOf(
                "Ideal como pré-exaustão ou finalizador sem fadigar o bíceps.",
                "Mantenha o abdômen contraído para evitar hiperextensão lombar."
            ),
            commonMistakes = listOf(
                "Dobrar os cotovelos e empurrar como tríceps pulley.",
                "Subir pouco os braços perdendo o alongamento dorsal."
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
                "Apoie o tórax no encosto eliminando qualquer estresse lombar.",
                "Puxe as manoplas fechando as escápulas no topo por 1 segundo.",
                "Estenda completamente os braços no final da descida."
            ),
            biomechanicsTips = listOf(
                "Sem fadiga lombar, permite treinar as costas até a falha técnica real."
            ),
            commonMistakes = listOf(
                "Descolar o peito do apoio para roubar na subida."
            )
        ),
        Exercise(
            id = "back_deadlift_conventional",
            name = "Levantamento Terra Convencional",
            muscleGroup = MuscleGroup.BACK,
            motionPattern = MotionPattern.DEADLIFT,
            primaryTarget = "Cadeia Posterior Completa (Eretores, Glúteos, Posteriores e Trapézio)",
            synergists = listOf("Quadríceps", "Latíssimo do Dorso", "Antebraços", "Core"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Avançado",
            recommendedReps = "3–6 reps",
            tempoTUT = "2-1-1-0",
            restSeconds = 210,
            executionSteps = listOf(
                "Posicione os pés na largura dos quadris com a barra sobre o meio do pé (2–3 cm da canela).",
                "Flexione o quadril, segure a barra logo fora das pernas, tire a folga da barra e trave o peito alto.",
                "Empurre o chão com as pernas enquanto mantém a barra colada ao corpo até travar quadril e joelhos.",
                "Desça controladamente devolvendo o quadril para trás antes de dobrar os joelhos."
            ),
            biomechanicsTips = listOf(
                "O movimento mais completo de força bruta do corpo humano.",
                "Mantenha a coluna rigorosamente neutra em toda a repetição."
            ),
            commonMistakes = listOf(
                "Arredondar a coluna lombar na saída do chão.",
                "Deixar a barra afastar das canelas."
            )
        ),

        // ==================== OMBROS (SHOULDERS) ====================
        Exercise(
            id = "shld_overhead_press_dumbbell",
            name = "Desenvolvimento Militar com Halteres Sentado",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.OVERHEAD_PRESS,
            primaryTarget = "Deltoide Anterior e Lateral",
            synergists = listOf("Tríceps Braquial", "Trapézio Superior", "Serrátil Anterior"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-0-1-0",
            restSeconds = 120,
            executionSteps = listOf(
                "Sente-se no banco a 80–85° com lombar bem apoiada e pés firmes.",
                "Posicione os halteres na altura das orelhas com cotovelos levemente à frente do plano escapular (~30°).",
                "Empurre os halteres acima da cabeça sem bater um no outro no topo.",
                "Desça controladamente até os halteres alinharem com o queixo/orelhas."
            ),
            biomechanicsTips = listOf(
                "Trabalhar no plano escapular (cotovelos 30° à frente) protege o espaço subacromial do ombro.",
                "Evite arquear excessivamente a lombar no final da série."
            ),
            commonMistakes = listOf(
                "Encurtar a descida parando no topo da cabeça.",
                "Jogar os cotovelos totalmente abertos para trás."
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
                "Em pé com tronco inclinado 10° à frente, segure os halteres ao lado das coxas.",
                "Eleve os braços lateralmente no plano escapular até a altura dos ombros, liderando com os cotovelos.",
                "Mantenha os punhos neutros (evite girar o polegar para baixo).",
                "Desça lentamente resistindo à gravidade sem deixar os halteres descansarem nas pernas."
            ),
            biomechanicsTips = listOf(
                "Pense em afastar os halteres para as paredes laterais da sala, não em levantá-los para o teto.",
                "O deltoide lateral responde excepcionalmente bem a altas repetições e frequência 3–4x/semana."
            ),
            commonMistakes = listOf(
                "Balançar o quadril para impulsionar pesos excessivos.",
                "Encolher o trapézio antes de iniciar a abdução do braço."
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
                "Ajuste a polia na altura do punho/joelho e segure o pegador cruzando à frente do corpo.",
                "Com a mão livre segurando a estrutura da máquina, incline-se levemente para o lado oposto.",
                "Abduza o braço até a linha paralela ao solo com tensão contínua desde o grau zero.",
                "Controle a descida até o punho cruzar levemente à frente do quadril."
            ),
            biomechanicsTips = listOf(
                "Ao contrário do halter, o cabo maximiza a tensão na posição alongada.",
                "Excelente para corrigir assimetrias entre ombro esquerdo e direito."
            ),
            commonMistakes = listOf(
                "Girar o tronco durante a elevação.",
                "Subir o braço muito acima da linha do ombro."
            )
        ),
        Exercise(
            id = "shld_reverse_pec_deck",
            name = "Crucifixo Invertido na Máquina (Posterior)",
            muscleGroup = MuscleGroup.SHOULDERS,
            motionPattern = MotionPattern.REVERSE_FLY,
            primaryTarget = "Deltoide Posterior",
            synergists = listOf("Infraespinal", "Redondo Menor", "Romboides", "Trapézio Médio"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante",
            recommendedReps = "12–18 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Sente-se de frente para o encosto do Peck Deck com o peito apoiado.",
                "Segure as manoplas horizontais com pegada neutra e cotovelos quase estendidos.",
                "Abra os braços para trás em arco mantendo os ombros deprimidos.",
                "Pare quando os braços alinharem com o tronco e retorne lentamente."
            ),
            biomechanicsTips = listOf(
                "Evite juntar demais as escápulas no final para que o trapézio não roube o trabalho do deltoide posterior."
            ),
            commonMistakes = listOf(
                "Puxar excessivamente para trás fechando as escápulas como uma remada.",
                "Usar carga alta demais gerando impulso cervical."
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
                "Ajuste a polia na altura do rosto e segure a corda com polegares apontando para trás.",
                "Puxe o centro da corda em direção à testa enquanto abre as pontas e realiza rotação externa dos ombros.",
                "No final do movimento, os punhos devem estar alinhados ou atrás das orelhas.",
                "Segure 2 segundos e retorne controladamente."
            ),
            biomechanicsTips = listOf(
                "Exercício indispensável para saúde articular dos ombros e equilíbrio postural."
            ),
            commonMistakes = listOf(
                "Puxar a corda em direção ao peito sem rotação externa.",
                "Deixar os cotovelos caírem abaixo da linha dos punhos."
            )
        ),

        // ==================== BÍCEPS (BICEPS) ====================
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
                "Segure a barra W na angulação ergonômica confortável com cotovelos fixos ao lado do tronco.",
                "Flexione os cotovelos elevando a barra até a contração máxima sem projetar os cotovelos à frente.",
                "Contraia o bíceps no topo por 1 segundo.",
                "Desça em 3 segundos até a extensão quase completa dos cotovelos."
            ),
            biomechanicsTips = listOf(
                "A barra W reduz o estresse em valgo nos punhos comparado à barra reta.",
                "Mantenha glúteos e abdômen contraídos para eliminar balanço."
            ),
            commonMistakes = listOf(
                "Jogar o tronco para trás no início da repetição.",
                "Parar a descida na metade do caminho."
            )
        ),
        Exercise(
            id = "bi_incline_dumbbell_curl",
            name = "Rosca Inclinada com Halteres no Banco (45–60°)",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.BICEP_CURL,
            primaryTarget = "Bíceps Braquial (Ênfase na Cabeça Longa — Pico)",
            synergists = listOf("Braquial"),
            equipment = EquipmentType.DUMBBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "10–14 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Sente-se em um banco inclinado a 45–60° e deixe os braços pendurados verticalmente atrás da linha do tronco.",
                "Mantendo os cotovelos apontados para o chão, flexione os braços supinando os punhos.",
                "Suba até a contração total sem puxar os cotovelos para frente.",
                "Desça totalmente até sentir o alongamento profundo na cabeça longa do bíceps."
            ),
            biomechanicsTips = listOf(
                "A extensão do ombro coloca a cabeça longa do bíceps em estiramento máximo sob carga."
            ),
            commonMistakes = listOf(
                "Avançar os cotovelos para frente anulando o alongamento.",
                "Descolar a cabeça e os ombros do banco."
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
                "Segure os halteres em pegada neutra ao lado do corpo.",
                "Flexione os cotovelos mantendo o punho neutro durante todo o trajeto.",
                "Suba até a altura do ombro sem balançar o tronco.",
                "Desça de forma controlada até estender os braços."
            ),
            biomechanicsTips = listOf(
                "O músculo braquial empurra o bíceps para cima aumentando o volume total do braço."
            ),
            commonMistakes = listOf(
                "Girar o punho para supinação durante a subida.",
                "Usar impulso de ombro."
            )
        ),
        Exercise(
            id = "bi_preacher_curl",
            name = "Rosca Scott na Máquina ou Banco",
            muscleGroup = MuscleGroup.BICEPS,
            motionPattern = MotionPattern.PREACHER_CURL,
            primaryTarget = "Bíceps Braquial (Ênfase na Cabeça Curta)",
            synergists = listOf("Braquial"),
            equipment = EquipmentType.MACHINE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Iniciante / Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "Ajuste o assento para que as axilas encaixem confortavelmente no topo do apoio.",
                "Segure a barra ou manopla com palmas para cima.",
                "Flexione até próximo de 90° sem descansar no topo.",
                "Desça lentamente até quase estender o cotovelo."
            ),
            biomechanicsTips = listOf(
                "Bloqueia completamente qualquer roubo do ombro."
            ),
            commonMistakes = listOf(
                "Soltar o peso na descida causando hiperextensão articular."
            )
        ),

        // ==================== TRÍCEPS (TRICEPS) ====================
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
                "Em pé frente à polia alta, incline o tronco levemente e trave os cotovelos ao lado das costelas.",
                "Estenda os cotovelos para baixo abrindo as pontas da corda no final do movimento.",
                "Contraia intensamente o tríceps por 1 segundo.",
                "Suba os antebraços até ultrapassar levemente 90° de flexão."
            ),
            biomechanicsTips = listOf(
                "Manter os cotovelos imóveis como dobradiças isola o tríceps."
            ),
            commonMistakes = listOf(
                "Deixar os cotovelos abrirem ou irem para trás e para frente."
            )
        ),
        Exercise(
            id = "tri_overhead_cable_extension",
            name = "Tríceps Francês na Polia (Acima da Cabeça)",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.OVERHEAD_TRICEP,
            primaryTarget = "Tríceps Braquial (Ênfase na Cabeça Longa)",
            synergists = listOf("Ancôneo"),
            equipment = EquipmentType.CABLE,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "10–15 reps",
            tempoTUT = "3-1-1-1",
            restSeconds = 75,
            executionSteps = listOf(
                "De costas para a polia ajustada na altura do quadril, segure a corda acima da cabeça.",
                "Incline o tronco à frente em base alternada com cotovelos apontados para cima.",
                "Flexione os cotovelos deixando as mãos descerem atrás da nuca para máximo alongamento.",
                "Estenda os cotovelos completamente contraindo a cabeça longa do tríceps."
            ),
            biomechanicsTips = listOf(
                "Elevar os braços acima da cabeça coloca a cabeça longa em estiramento ideal para hipertrofia."
            ),
            commonMistakes = listOf(
                "Abrir demais os cotovelos para os lados."
            )
        ),
        Exercise(
            id = "tri_skullcrusher_ez",
            name = "Tríceps Testa com Barra W",
            muscleGroup = MuscleGroup.TRICEPS,
            motionPattern = MotionPattern.SKULLCRUSHER,
            primaryTarget = "Tríceps Braquial (Todas as Cabeças)",
            synergists = listOf("Ancôneo", "Deltoide Anterior"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.ISOLATION,
            difficulty = "Intermediário",
            recommendedReps = "8–12 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 90,
            executionSteps = listOf(
                "Deitado no banco reto, segure a barra W com os braços inclinados ~15° para trás da vertical.",
                "Flexione apenas os cotovelos descendo a barra em direção ao topo/atrás da cabeça.",
                "Estenda os cotovelos mantendo os braços na mesma angulação.",
                "Repita com controle total."
            ),
            biomechanicsTips = listOf(
                "Manter os braços ligeiramente inclinados para trás mantém tensão constante no topo."
            ),
            commonMistakes = listOf(
                "Abrir os cotovelos excessivamente."
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
                "Deite-se no banco reto e segure a barra na largura exata dos ombros.",
                "Desça a barra mantendo os cotovelos rentes ao tronco até tocar a linha inferior do peito.",
                "Empurre com foco na extensão dos cotovelos até o topo."
            ),
            biomechanicsTips = listOf(
                "Principal exercício composto para sobrecarga progressiva pesada no tríceps."
            ),
            commonMistakes = listOf(
                "Pegada estreita demais gerando dor nos punhos."
            )
        ),

        // ==================== QUADRÍCEPS (QUADS) ====================
        Exercise(
            id = "quad_high_bar_squat",
            name = "Agachamento Livre High-Bar",
            muscleGroup = MuscleGroup.QUADS,
            motionPattern = MotionPattern.SQUAT,
            primaryTarget = "Quadríceps Femoral (Vasto Lateral, Medial, Intermédio e Reto)",
            synergists = listOf("Glúteo Máximo", "Adutor Magno", "Eretores da Espinha", "Core"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "5–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 180,
            executionSteps = listOf(
                "Apoie a barra sobre o trapézio superior, pés na largura dos ombros com pontas 15–25° para fora.",
                "Inspire profundamente fazendo bracing abdominal 360°.",
                "Inicie flexionando joelhos e quadris simultaneamente, descendo até quebra da paralela.",
                "Empurre o chão mantendo o tronco o mais vertical possível até a extensão completa."
            ),
            biomechanicsTips = listOf(
                "A projeção anterior dos joelhos sobre a ponta dos pés é natural, segura e maximiza o torque no quadríceps."
            ),
            commonMistakes = listOf(
                "Deixar os joelhos caírem para dentro (valgo dinâmico) na subida."
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
                "Posicione os pés na parte média-baixa da plataforma na largura dos quadris.",
                "Destrave a plataforma e segure firme nas alças laterais puxando o quadril contra o banco.",
                "Desça até o máximo ângulo de flexão sem que o sacro/lombar descole do encosto.",
                "Empurre sem hiperestender os joelhos no topo."
            ),
            biomechanicsTips = listOf(
                "Pés mais baixos na plataforma aumentam o momento flexor no joelho e priorizam o quadríceps."
            ),
            commonMistakes = listOf(
                "Descer tanto que a lombar enrola e sai do banco."
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
                "Apoie ombros e costas firmemente no encosto e posicione os pés na base da plataforma.",
                "Destrave a máquina e desça profundamente com controle de 3 segundos.",
                "Faça uma pausa breve de 1 segundo no fundo para eliminar o reflexo elástico.",
                "Suba explosivamente até quase estender os joelhos."
            ),
            biomechanicsTips = listOf(
                "Um dos melhores construtores de quadríceps da musculação pela estabilidade total."
            ),
            commonMistakes = listOf(
                "Tirar os calcanhares da plataforma na descida."
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
                "Alinhe o eixo de rotação do joelho exatamente com o eixo da máquina.",
                "Ajuste o rolo inferior logo acima dos tornozelos.",
                "Estenda completamente os joelhos e segure 1–2 segundos no topo.",
                "Desça lentamente até a flexão total antes de iniciar a próxima repetição."
            ),
            biomechanicsTips = listOf(
                "Por ser biarticular, o reto femoral precisa da extensora para desenvolvimento completo."
            ),
            commonMistakes = listOf(
                "Dar chutes rápidos usando impulso."
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
                "Apoie o peito do pé traseiro em um banco atrás de você e dê um passo firme à frente.",
                "Mantenha o tronco mais ereto para focar quadríceps.",
                "Desça até o joelho traseiro quase tocar o chão.",
                "Empurre através do meio do pé dianteiro até retornar ao topo."
            ),
            biomechanicsTips = listOf(
                "Elimina desequilíbrios entre pernas e gera alto estresse mecânico."
            ),
            commonMistakes = listOf(
                "Colocar os dois pés na mesma linha perdendo o equilíbrio."
            )
        ),

        // ==================== POSTERIORES DE COXA (HAMSTRINGS) ====================
        Exercise(
            id = "ham_rdl_barbell",
            name = "Stiff / Levantamento Terra Romeno (RDL)",
            muscleGroup = MuscleGroup.HAMSTRINGS,
            motionPattern = MotionPattern.RDL_HINGE,
            primaryTarget = "Isquiotibiais (Bíceps Femoral, Semitendíneo e Semimembranáceo)",
            synergists = listOf("Glúteo Máximo", "Eretores da Espinha", "Antebraços"),
            equipment = EquipmentType.BARBELL,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário",
            recommendedReps = "6–10 reps",
            tempoTUT = "3-1-1-0",
            restSeconds = 150,
            executionSteps = listOf(
                "Segure a barra na largura dos ombros, destrave levemente os joelhos (10–15° fixos).",
                "Empurre o quadril diretamente para trás mantendo a barra raspando nas coxas e canelas.",
                "Desça até o limite de flexibilidade dos posteriores sem arredondar a lombar.",
                "Contraia glúteos e posteriores para empurrar o quadril de volta à posição vertical."
            ),
            biomechanicsTips = listOf(
                "O movimento deve parar assim que o quadril parar de ir para trás."
            ),
            commonMistakes = listOf(
                "Afastar a barra das pernas, multiplicando o torque na coluna lombar."
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
                "Ajuste o encosto e trave firmemente o apoio sobre as coxas.",
                "Flexione os joelhos puxando o rolo o máximo possível para baixo do banco.",
                "Controle o retorno por 3 segundos até a extensão total."
            ),
            biomechanicsTips = listOf(
                "A cadeira flexora gera maior hipertrofia porque o quadril flexionado mantém os isquiotibiais pré-alongados."
            ),
            commonMistakes = listOf(
                "Deixar o apoio das coxas frouxo fazendo o quadril subir."
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
                "Deite-se de bruços mantendo o quadril pressionado contra o banco.",
                "Puxe o rolo em direção aos glúteos com dorsiflexão dos tornozelos.",
                "Contraia no topo por 1 segundo sem levantar o quadril."
            ),
            biomechanicsTips = listOf(
                "Pressionar o púbis contra o banco evita que os flexores do quadril roubem."
            ),
            commonMistakes = listOf(
                "Levantar o bumbum do banco durante a subida."
            )
        ),

        // ==================== GLÚTEOS (GLUTES) ====================
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
                "Apoie a base das escápulas em um banco estável e posicione a barra acolchoada sobre o quadril.",
                "Deixe os pés na largura dos ombros de modo que as canelas fiquem verticais (90°) no topo.",
                "Empurre pelos calcanhares elevando o quadril até alinhar ombros, quadril e joelhos.",
                "Faça retroversão pélvica no topo por 2 segundos e desça controladamente."
            ),
            biomechanicsTips = listOf(
                "Mantenha o queixo apontado para o peito para evitar hiperextensão lombar."
            ),
            commonMistakes = listOf(
                "Jogar a cabeça para trás e hiperestender a lombar no topo."
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
                "Prenda a tornozeleira na polia baixa e incline o tronco ~30°.",
                "Estenda o quadril para trás e levemente para fora (~15°).",
                "Pare assim que atingir a contração máxima do glúteo sem girar a pelve."
            ),
            biomechanicsTips = listOf(
                "A leve abdução de 15° acompanha a orientação natural das fibras do glúteo máximo."
            ),
            commonMistakes = listOf(
                "Subir demais a perna compensando com a coluna lombar."
            )
        ),

        // ==================== PANTURRILHAS (CALVES) ====================
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
                "Apoie a ponta dos pés na plataforma mantendo os joelhos estendidos.",
                "Desça os calcanhares até o alongamento máximo e FAÇA PAUSA DE 2 SEGUNDOS no fundo.",
                "Suba na ponta dos pés o mais alto possível contraindo no topo por 1 segundo."
            ),
            biomechanicsTips = listOf(
                "A pausa de 2 segundos elimina o efeito mola do tendão de Aquiles e multiplica a hipertrofia."
            ),
            commonMistakes = listOf(
                "Fazer repetições rápidas quicando."
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
                "Com joelhos flexionados a 90°, ajuste o apoio firme sobre o final das coxas.",
                "Desça os calcanhares ao máximo com pausa de 2 segundos.",
                "Eleve os calcanhares até a flexão plantar máxima."
            ),
            biomechanicsTips = listOf(
                "Com o joelho dobrado a 90°, o sóleo assume o protagonismo."
            ),
            commonMistakes = listOf(
                "Não pausar no alongamento inferior."
            )
        ),

        // ==================== ABDÔMEN & CORE (CORE) ====================
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
                "Ajoelhe-se frente à polia alta segurando a corda ao lado das têmporas.",
                "Mantenha o quadril fixo acima dos joelhos.",
                "Enrole a coluna torácica levando os cotovelos em direção às coxas enquanto expira.",
                "Retorne estendendo a coluna sem sentar nos calcanhares."
            ),
            biomechanicsTips = listOf(
                "Permite progredir carga como qualquer outro músculo."
            ),
            commonMistakes = listOf(
                "Sentar nos calcanhares usando os flexores do quadril."
            )
        ),
        Exercise(
            id = "core_hanging_leg_raise",
            name = "Elevação de Pernas Suspenso na Barra",
            muscleGroup = MuscleGroup.CORE,
            motionPattern = MotionPattern.LEG_RAISE,
            primaryTarget = "Reto Abdominal (Porção Infra)",
            synergists = listOf("Oblíquos", "Antebraços"),
            equipment = EquipmentType.BODYWEIGHT,
            mechanics = MechanicsType.COMPOUND,
            difficulty = "Intermediário / Avançado",
            recommendedReps = "10–15 reps",
            tempoTUT = "2-1-1-1",
            restSeconds = 60,
            executionSteps = listOf(
                "Pendure-se na barra fixa com ombros ativos.",
                "Eleve as pernas e ENROLE A PELVE para cima no final.",
                "Desça lentamente evitando balançar o corpo."
            ),
            biomechanicsTips = listOf(
                "O reto abdominal atua nos últimos graus quando a pelve faz retroversão."
            ),
            commonMistakes = listOf(
                "Balançar o corpo como um pêndulo."
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
                "Ajoelhe-se, segure a roda com braços estendidos e contraia glúteos e abdômen.",
                "Role lentamente à frente mantendo a lombar neutra.",
                "Retorne puxando com o abdômen e dorsais sem arquear a coluna."
            ),
            biomechanicsTips = listOf(
                "Altíssima ativação eletromiográfica de todo o reto abdominal e transverso."
            ),
            commonMistakes = listOf(
                "Deixar a barriga afundar e hiperestender a lombar."
            )
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
