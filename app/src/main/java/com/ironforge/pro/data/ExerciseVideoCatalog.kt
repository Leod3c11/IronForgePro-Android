package com.ironforge.pro.data

import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.MotionPattern

data class ExerciseVideoDemo(
    val youtubeVideoId: String,
    val title: String,
    val instructor: String,
    val durationLabel: String
) {
    val watchUrl: String
        get() = "https://www.youtube.com/watch?v=$youtubeVideoId"

    val thumbnailUrl: String
        get() = "https://i.ytimg.com/vi/$youtubeVideoId/hqdefault.jpg"

    val embedUrl: String
        get() = "https://www.youtube.com/embed/$youtubeVideoId?autoplay=1&rel=0&modestbranding=1&playsinline=1"
}

object ExerciseVideoCatalog {

    private val byExerciseId: Map<String, ExerciseVideoDemo> = mapOf(
        // CHEST
        "chest_bench_barbell" to ExerciseVideoDemo("EZMYCLKuGow", "Supino Reto com Barra — Execução Correta", "Leandro Twin", "2:01"),
        "chest_bench_dumbbell" to ExerciseVideoDemo("50RSzhMG5Hc", "Supino Reto — Técnica & Escápulas", "Fernando Cantarelli", "0:19"),
        "chest_incline_barbell" to ExerciseVideoDemo("6jBx5YwAb7E", "Supino Inclinado com Barra — Técnica", "Jézer Trevisan", "0:15"),
        "chest_incline_dumbbell" to ExerciseVideoDemo("Fa-X2ByLHaY", "Supino Inclinado com Halteres (30°)", "Rafa Menegocci", "0:38"),
        "chest_incline_machine" to ExerciseVideoDemo("JM1yJGN-hKQ", "Técnica Perfeita no Supino Inclinado", "Fabricio Pacholok", "0:28"),
        "chest_smith_incline_press" to ExerciseVideoDemo("JM1yJGN-hKQ", "Supino Inclinado no Smith (30°)", "Fabricio Pacholok", "0:28"),
        "chest_decline_barbell" to ExerciseVideoDemo("MPM1bqU7b44", "Execução Correta do Supino", "Julio Balestrin", "0:59"),
        "chest_dumbbell_fly" to ExerciseVideoDemo("YyFaD_mt8kQ", "Crucifixo & Adução Peitoral", "Leandro Twin", "0:38"),
        "chest_pullover_dumbbell" to ExerciseVideoDemo("YyFaD_mt8kQ", "Pullover com Halter — Expansão Torácica", "Leandro Twin", "0:38"),
        "chest_cable_crossover_high" to ExerciseVideoDemo("nuTuKjcQRHg", "Técnica no Crossover Polia Alta", "Laércio Refundini", "0:53"),
        "chest_cable_crossover_low" to ExerciseVideoDemo("poe7X02U2qw", "Crossover na Polia — Execução", "Miqueias Alves", "0:15"),
        "chest_pec_deck" to ExerciseVideoDemo("9BEQWr0u-ac", "Crucifixo Máquina / Peck Deck em 5 Passos", "Laércio Refundini", "0:23"),
        "chest_dips_weighted" to ExerciseVideoDemo("1P0HiOYDadE", "Paralelas — Execução e Postura", "Leandro Twin", "0:41"),
        "chest_weighted_pushup" to ExerciseVideoDemo("LJz40nTE_sI", "Empurrar Corporal — Técnica", "Leandro Twin", "1:02"),

        // BACK
        "back_pull_up" to ExerciseVideoDemo("frL-lkKXHgM", "Barra Fixa Pronada — Execução Completa", "Laércio Refundini", "1:05"),
        "back_chin_up" to ExerciseVideoDemo("UB6fhWvB4dY", "Barra Fixa para Hipertrofia das Costas", "Leandro Twin", "6:26"),
        "back_lat_pulldown" to ExerciseVideoDemo("SapM-j_3M20", "Puxada Aberta no Pulley — Passo a Passo", "Isabella Duarte", "0:23"),
        "back_lat_pulldown_neutral" to ExerciseVideoDemo("4-PsgYmkDsM", "Puxada Alta Perfeita — Ativação Dorsal", "Hosaka Personal", "0:19"),
        "back_single_arm_pulldown" to ExerciseVideoDemo("y4odSs51DBE", "Como Ativar Mais as Dorsais na Puxada", "Ana Sensolo", "0:49"),
        "back_barbell_row" to ExerciseVideoDemo("TfxJMertfsw", "Remada Curvada com Barra — Execução", "Leandro Twin", "2:24"),
        "back_t_bar_row" to ExerciseVideoDemo("TfxJMertfsw", "Remada T-Bar / Cavalinho Livre", "Leandro Twin", "2:24"),
        "back_pendlay_row" to ExerciseVideoDemo("cMQpvkZGQrE", "Remada Curvada — Postura e Coluna", "Guilherme Ern", "0:46"),
        "back_one_arm_dumbbell_row" to ExerciseVideoDemo("TZpDTv1Xmzc", "Remada Unilateral — Correção de Erros", "Leandro Twin", "0:40"),
        "back_meadows_row" to ExerciseVideoDemo("TZpDTv1Xmzc", "Remada Unilateral — Postura Escapular", "Leandro Twin", "0:40"),
        "back_seated_cable_row" to ExerciseVideoDemo("3kqjkP7kqVM", "Remada Baixa com Triângulo", "Hosaka Personal", "0:43"),
        "back_wide_grip_seated_row" to ExerciseVideoDemo("3kqjkP7kqVM", "Remada Baixa Pegada Aberta", "Hosaka Personal", "0:43"),
        "back_chest_supported_row" to ExerciseVideoDemo("fZzybnfRhqA", "Remada Apoiada — Retração Escapular", "Laércio Refundini", "0:21"),
        "back_straight_arm_pulldown" to ExerciseVideoDemo("dnZU5bkgfiY", "Pulldown com Corda na Polia Alta", "Isabella Duarte", "1:01"),
        "back_deadlift_conventional" to ExerciseVideoDemo("50AkPBZwACQ", "Levantamento Terra — Passo a Passo", "Leandro Twin", "1:29"),
        "back_rack_pull" to ExerciseVideoDemo("ASO89bLkLqU", "Execução Segura do Levantamento Terra", "Fabricio Pacholok", "0:42"),

        // SHOULDERS
        "shld_barbell_ohp" to ExerciseVideoDemo("th7n97gZm9A", "Desenvolvimento Militar — Técnica", "Tay Training", "0:53"),
        "shld_overhead_press_dumbbell" to ExerciseVideoDemo("Wywt3mN-6RA", "Desenvolvimento com Halteres Sentado", "Fernando Cantarelli", "0:24"),
        "shld_arnold_press" to ExerciseVideoDemo("0tkYjwEOoSs", "Desenvolvimento com Halteres — Plano Escapular", "Ana Sensolo", "0:25"),
        "shld_machine_press" to ExerciseVideoDemo("DFXtzdXN_iY", "Desenvolvimento Ombros — Execução", "Marcus Menezes", "0:14"),
        "shld_lateral_raise_dumbbell" to ExerciseVideoDemo("nhQ4mdk0TmM", "A Elevação Lateral Perfeita em 7 Passos", "Laércio Refundini", "1:00"),
        "shld_cable_lateral_raise" to ExerciseVideoDemo("7odpb3ONhuc", "Elevação Lateral — Sem Roubar Trapézio", "Tay Training", "0:35"),
        "shld_lateral_cable_behind_back" to ExerciseVideoDemo("7odpb3ONhuc", "Elevação Lateral na Polia por Trás", "Tay Training", "0:35"),
        "shld_upright_row_cable" to ExerciseVideoDemo("nhQ4mdk0TmM", "Deltoide Lateral — Técnica e Amplitude", "Laércio Refundini", "1:00"),
        "shld_machine_lateral_raise" to ExerciseVideoDemo("ORparUDksUk", "Elevação Lateral — Biomecânica", "FisioPrev", "1:22"),
        "shld_front_raise" to ExerciseVideoDemo("Wywt3mN-6RA", "Elevação Anterior / Deltoide Frontal", "Fernando Cantarelli", "0:24"),
        "shld_reverse_pec_deck" to ExerciseVideoDemo("xbvVqbKvUBc", "Crucifixo Invertido na Máquina", "Open Sport Center", "0:14"),
        "shld_bent_over_rear_delt" to ExerciseVideoDemo("C9Q9so5Fqws", "Crucifixo Inverso com Halteres", "Matheus Nascimento", "0:20"),
        "shld_face_pull" to ExerciseVideoDemo("kamdxk0L4Yw", "Face Pull na Polia com Corda", "Isabella Duarte", "0:53"),

        // BICEPS
        "bi_straight_bar_curl" to ExerciseVideoDemo("Yp-SuCCqui8", "Rosca Direta — Execução Correta", "Laércio Refundini", "1:15"),
        "bi_barbell_ez_curl" to ExerciseVideoDemo("2AWV2FwL1go", "Rosca Direta na Barra W", "Laércio Refundini", "1:09"),
        "bi_alternating_dumbbell_curl" to ExerciseVideoDemo("KX1mEovB8C8", "Pegada e Supinação na Rosca", "Laércio Refundini", "0:25"),
        "bi_zottman_curl" to ExerciseVideoDemo("Yp-SuCCqui8", "Rosca Zottman — Bíceps & Antebraço", "Laércio Refundini", "1:15"),
        "bi_incline_dumbbell_curl" to ExerciseVideoDemo("I8T_rJIn1HY", "Rosca Bíceps — Controle Excêntrico", "Isabella Duarte", "0:35"),
        "bi_bayesian_cable_curl" to ExerciseVideoDemo("Un8zsUZmSb8", "Bíceps — Alongamento e Contração", "Vane Fit", "1:09"),
        "bi_preacher_curl" to ExerciseVideoDemo("GTcQ45RrHNY", "Rosca Scott — Execução Sem Erros", "Laércio Refundini", "0:45"),
        "bi_spider_curl" to ExerciseVideoDemo("GTcQ45RrHNY", "Rosca Spider / Scott — Pico de Contração", "Laércio Refundini", "0:45"),
        "bi_concentration_curl" to ExerciseVideoDemo("JWRoHF7I2NQ", "Rosca Scott / Concentrada", "Via Brasil Club", "0:47"),
        "bi_hammer_curl" to ExerciseVideoDemo("8PN6YfFC6Q4", "Rosca Martelo com Halteres", "Wagner Durigon", "0:21"),
        "bi_rope_hammer_curl" to ExerciseVideoDemo("KIOh2dh77Pg", "Rosca Martelo — Técnica", "Elite da Musculação", "0:42"),

        // TRICEPS
        "tri_close_grip_bench" to ExerciseVideoDemo("EZMYCLKuGow", "Supino Fechado / Empurrar", "Leandro Twin", "2:01"),
        "tri_jm_press" to ExerciseVideoDemo("8LFsUN913r4", "JM Press / Extensão Pesada", "Marcus Menezes", "0:13"),
        "tri_dips_upright" to ExerciseVideoDemo("1P0HiOYDadE", "Paralelas Foco Tríceps", "Leandro Twin", "0:41"),
        "tri_dip_machine" to ExerciseVideoDemo("LJz40nTE_sI", "Mergulho Paralelas — Execução", "Leandro Twin", "1:02"),
        "tri_skullcrusher_ez" to ExerciseVideoDemo("8LFsUN913r4", "Tríceps Testa com Barra W", "Marcus Menezes", "0:13"),
        "tri_dumbbell_skullcrusher" to ExerciseVideoDemo("0jMF0o0DdbA", "Tríceps Testa — Ajuste Articular", "Laércio Refundini", "0:33"),
        "tri_overhead_cable_extension" to ExerciseVideoDemo("IoawExktILM", "Tríceps Francês com Corda na Polia", "Team Olympus", "0:19"),
        "tri_overhead_dumbbell_extension" to ExerciseVideoDemo("wcpCGLuXFfU", "Tríceps Francês — Cabeça Longa", "Hosaka Personal", "0:17"),
        "tri_cable_pushdown_rope" to ExerciseVideoDemo("Lv1uMTWQ0Ww", "Tríceps Pulley com Corda", "Laércio Refundini", "1:14"),
        "tri_cable_pushdown_bar" to ExerciseVideoDemo("jsntw4tVAgQ", "Tríceps Pulley — Postura de Cotovelos", "Tay Training", "0:33"),
        "tri_kickback_cable" to ExerciseVideoDemo("Lv1uMTWQ0Ww", "Tríceps na Polia — Contração Máxima", "Laércio Refundini", "1:14"),

        // QUADS
        "quad_high_bar_squat" to ExerciseVideoDemo("s7i94Okznns", "Agachamento Livre — Técnica Completa", "Rodrigo Zago", "1:42"),
        "quad_front_squat" to ExerciseVideoDemo("wzsUfTMPrEg", "Segredos do Agachamento Perfeito", "Fernando Cantarelli", "1:00"),
        "quad_goblet_squat" to ExerciseVideoDemo("s7i94Okznns", "Agachamento Goblet — Profundidade", "Rodrigo Zago", "1:42"),
        "quad_sissy_squat" to ExerciseVideoDemo("tcdgal1epsI", "Sissy Squat / Reto Femoral", "Leandro Twin", "0:53"),
        "quad_hack_squat" to ExerciseVideoDemo("LK-ktgN3S1w", "Agachamento Guiado — Ativação Quadríceps", "Hosaka Personal", "0:19"),
        "quad_smith_squat" to ExerciseVideoDemo("LK-ktgN3S1w", "Agachamento no Smith — Posição dos Pés", "Hosaka Personal", "0:19"),
        "quad_leg_press_45" to ExerciseVideoDemo("lHZUF_s3q9c", "Leg Press 45° — Execução e Amplitude", "Leandro Twin", "0:57"),
        "quad_leg_press_horizontal" to ExerciseVideoDemo("EiltwxxREDc", "Leg Press — Execução Correta", "Oficina do Corpo", "0:22"),
        "quad_leg_extension" to ExerciseVideoDemo("tcdgal1epsI", "Cadeira Extensora — Regulagem e Pico", "Leandro Twin", "0:53"),
        "quad_bulgarian_split_squat" to ExerciseVideoDemo("F6U-nsRnFWA", "Agachamento Búlgaro com Halteres", "Laércio Refundini", "1:16"),
        "quad_walking_lunge" to ExerciseVideoDemo("v-bItsW7fUI", "Avanço / Búlgaro — Passo a Passo", "Hosaka Personal", "0:21"),

        // HAMSTRINGS
        "ham_rdl_barbell" to ExerciseVideoDemo("dta3aMVe9qA", "Levantamento Terra Romeno (RDL)", "Isabella Duarte", "0:36"),
        "ham_dumbbell_rdl" to ExerciseVideoDemo("pgn2uQMj6V8", "Stiff vs. Terra Romeno — Execução", "Tay Training", "0:14"),
        "ham_single_leg_rdl_dumbbell" to ExerciseVideoDemo("dta3aMVe9qA", "RDL Unilateral com Halteres", "Isabella Duarte", "0:36"),
        "ham_seated_leg_curl" to ExerciseVideoDemo("ERaXLpeTKlQ", "Cadeira Flexora — Técnica Completa", "Isabella Duarte", "0:57"),
        "ham_lying_leg_curl" to ExerciseVideoDemo("umxlbgCK6oc", "Mesa Flexora Deitada — Execução", "Ana Sensolo", "0:39"),
        "ham_standing_leg_curl" to ExerciseVideoDemo("c3cng1WqREQ", "Flexora — Ajuste e Controle", "Hosaka Personal", "0:11"),
        "ham_good_morning" to ExerciseVideoDemo("dta3aMVe9qA", "Dobradiça de Quadril (Posterior)", "Isabella Duarte", "0:36"),
        "ham_nordic_curl" to ExerciseVideoDemo("umxlbgCK6oc", "Flexão de Joelho — Posterior", "Ana Sensolo", "0:39"),

        // GLUTES
        "glute_hip_thrust" to ExerciseVideoDemo("6sTO9ejM-Ew", "Elevação Pélvica com Barra (Hip Thrust)", "Via Brasil Club", "0:37"),
        "glute_single_leg_hip_thrust" to ExerciseVideoDemo("MXgEkxHwWs8", "Elevação Pélvica — Ativação Máxima", "Luiz Salles", "0:33"),
        "glute_sumo_squat" to ExerciseVideoDemo("S_QO57EIKpk", "Base Aberta para Glúteos e Pernas", "Musculação com Marcos", "0:36"),
        "glute_cable_kickback" to ExerciseVideoDemo("MXgEkxHwWs8", "Extensão de Quadril Foco Glúteo", "Luiz Salles", "0:33"),
        "glute_abduction_machine" to ExerciseVideoDemo("MXgEkxHwWs8", "Glúteo Médio & Máximo", "Luiz Salles", "0:33"),
        "glute_cable_abduction" to ExerciseVideoDemo("MXgEkxHwWs8", "Abdução de Quadril", "Luiz Salles", "0:33"),

        // CALVES
        "calf_standing_machine" to ExerciseVideoDemo("BTRbfHj4a-4", "Panturrilha em Pé e Variações", "Renato Cariani", "0:59"),
        "calf_leg_press" to ExerciseVideoDemo("fmEBX2tbRq0", "Panturrilha — Amplitude e Pausa", "Tiozão do Fitness", "1:01"),
        "calf_seated_machine" to ExerciseVideoDemo("6eJ9EVwazXk", "Panturrilha Sentado — Execução", "Leandro Twin", "0:54"),
        "calf_single_leg_dumbbell" to ExerciseVideoDemo("QDM1QyLPkIk", "Panturrilha no Degrau — Técnica", "NS Personal", "0:12"),

        // CORE
        "core_cable_crunch" to ExerciseVideoDemo("Wfi1XdVh0wE", "Abdominal Supra na Polia Alta", "Malu Bacca", "1:18"),
        "core_hanging_leg_raise" to ExerciseVideoDemo("t8yggGcviS8", "Abdominal Infra Suspenso na Barra", "Mente Maromba", "1:00"),
        "core_dragon_flag_negatives" to ExerciseVideoDemo("t8yggGcviS8", "Core Avançado / Dragon Flag", "Mente Maromba", "1:00"),
        "core_captains_chair" to ExerciseVideoDemo("cCjfKQRJp2k", "Abdominal Infra — Retroversão Pélvica", "Josemar Swat", "0:42"),
        "core_ab_wheel" to ExerciseVideoDemo("DWFt7uGwJFQ", "Abdominal na Rodinha (Ab Wheel)", "Isabella Duarte", "1:01"),
        "core_weighted_plank" to ExerciseVideoDemo("XBFjXkgpJ0Q", "Core & Anti-Extensão Abdominal", "Guilherme Carvalho", "0:53"),
        "core_pallof_press" to ExerciseVideoDemo("DAgYXDuBPQA", "Abdominal na Polia — Estabilidade", "Isabella Duarte", "0:59")
    )

    private val byPatternFallback: Map<MotionPattern, ExerciseVideoDemo> = mapOf(
        MotionPattern.BENCH_PRESS to ExerciseVideoDemo("EZMYCLKuGow", "Supino Reto — Execução Correta", "Leandro Twin", "2:01"),
        MotionPattern.INCLINE_PRESS to ExerciseVideoDemo("Fa-X2ByLHaY", "Supino Inclinado — Execução", "Rafa Menegocci", "0:38"),
        MotionPattern.FLY_CROSSOVER to ExerciseVideoDemo("9BEQWr0u-ac", "Crucifixo / Peck Deck", "Laércio Refundini", "0:23"),
        MotionPattern.DIPS to ExerciseVideoDemo("1P0HiOYDadE", "Paralelas — Execução", "Leandro Twin", "0:41"),
        MotionPattern.PULL_UP to ExerciseVideoDemo("frL-lkKXHgM", "Barra Fixa — Execução", "Laércio Refundini", "1:05"),
        MotionPattern.LAT_PULLDOWN to ExerciseVideoDemo("SapM-j_3M20", "Puxada Frontal no Pulley", "Isabella Duarte", "0:23"),
        MotionPattern.ROW_HORIZONTAL to ExerciseVideoDemo("TfxJMertfsw", "Remada Curvada — Execução", "Leandro Twin", "2:24"),
        MotionPattern.STRAIGHT_ARM_PULL to ExerciseVideoDemo("dnZU5bkgfiY", "Pulldown com Corda", "Isabella Duarte", "1:01"),
        MotionPattern.OVERHEAD_PRESS to ExerciseVideoDemo("Wywt3mN-6RA", "Desenvolvimento Ombros", "Fernando Cantarelli", "0:24"),
        MotionPattern.LATERAL_RAISE to ExerciseVideoDemo("nhQ4mdk0TmM", "Elevação Lateral Perfeita", "Laércio Refundini", "1:00"),
        MotionPattern.REVERSE_FLY to ExerciseVideoDemo("kamdxk0L4Yw", "Posterior de Ombros / Face Pull", "Isabella Duarte", "0:53"),
        MotionPattern.BICEP_CURL to ExerciseVideoDemo("2AWV2FwL1go", "Rosca Direta — Execução", "Laércio Refundini", "1:09"),
        MotionPattern.PREACHER_CURL to ExerciseVideoDemo("GTcQ45RrHNY", "Rosca Scott — Execução", "Laércio Refundini", "0:45"),
        MotionPattern.TRICEP_PUSHDOWN to ExerciseVideoDemo("Lv1uMTWQ0Ww", "Tríceps Pulley Corda", "Laércio Refundini", "1:14"),
        MotionPattern.OVERHEAD_TRICEP to ExerciseVideoDemo("IoawExktILM", "Tríceps Francês Polia", "Team Olympus", "0:19"),
        MotionPattern.SKULLCRUSHER to ExerciseVideoDemo("8LFsUN913r4", "Tríceps Testa", "Marcus Menezes", "0:13"),
        MotionPattern.SQUAT to ExerciseVideoDemo("s7i94Okznns", "Agachamento Livre", "Rodrigo Zago", "1:42"),
        MotionPattern.LEG_PRESS to ExerciseVideoDemo("lHZUF_s3q9c", "Leg Press 45°", "Leandro Twin", "0:57"),
        MotionPattern.LEG_EXTENSION to ExerciseVideoDemo("tcdgal1epsI", "Cadeira Extensora", "Leandro Twin", "0:53"),
        MotionPattern.LUNGE_SPLIT to ExerciseVideoDemo("F6U-nsRnFWA", "Agachamento Búlgaro", "Laércio Refundini", "1:16"),
        MotionPattern.RDL_HINGE to ExerciseVideoDemo("dta3aMVe9qA", "Levantamento Terra Romeno", "Isabella Duarte", "0:36"),
        MotionPattern.LEG_CURL to ExerciseVideoDemo("ERaXLpeTKlQ", "Cadeira Flexora", "Isabella Duarte", "0:57"),
        MotionPattern.HIP_THRUST to ExerciseVideoDemo("6sTO9ejM-Ew", "Elevação Pélvica", "Via Brasil Club", "0:37"),
        MotionPattern.CALF_RAISE to ExerciseVideoDemo("6eJ9EVwazXk", "Panturrilha", "Leandro Twin", "0:54"),
        MotionPattern.CRUNCH_FLEXION to ExerciseVideoDemo("Wfi1XdVh0wE", "Abdominal Supra Polia", "Malu Bacca", "1:18"),
        MotionPattern.LEG_RAISE to ExerciseVideoDemo("t8yggGcviS8", "Abdominal Infra Barra", "Mente Maromba", "1:00"),
        MotionPattern.AB_WHEEL to ExerciseVideoDemo("DWFt7uGwJFQ", "Roda Abdominal", "Isabella Duarte", "1:01"),
        MotionPattern.DEADLIFT to ExerciseVideoDemo("50AkPBZwACQ", "Levantamento Terra", "Leandro Twin", "1:29")
    )

    fun resolveForExercise(exercise: Exercise): ExerciseVideoDemo {
        return byExerciseId[exercise.id]
            ?: byPatternFallback[exercise.motionPattern]
            ?: ExerciseVideoDemo("EZMYCLKuGow", exercise.name, "IronForge Coach", "1:30")
    }
}
