# 🏋️ IronForge Pro — Ciência, Hipertrofia & Performance

Aplicativo nativo Android desenvolvido em **Kotlin + Jetpack Compose (Material 3)** com design **Obsidian & Volt**, focado em musculação avançada, hipertrofia baseada em evidências, controle de volume semanal (MEV/MAV/MRV) e rastreamento de cargas em tempo real.

---

## ✨ Funcionalidades Principais

1. **Dashboard & Analytics de Performance**
   - Acompanhamento de **Tonelagem Total (Volume Load)**, treinos concluídos e sequência (streak).
   - **Gráfico Radar Hexagonal** de equilíbrio muscular e **Gráfico de Evolução de Volume** desenhados nativamente em Compose Canvas.
   - **Diário Corporal & Medidas**: Registro histórico de peso corporal, % de gordura, braço, peitoral, cintura, coxa e panturrilha.

2. **Programas de Treino Estruturados + Criador Personalizado**
   - **PPL Pro (Push / Pull / Legs — 6x/semana)**
   - **Upper / Lower (Força & Hipertrofia — 4x/semana)**
   - **ABCDE (Bodybuilding Clássico — 5x/semana)**
   - **Full Body (Força Base — 3x/semana)**
   - **Criador de Fichas**: Monte e salve suas próprias divisões personalizadas.

3. **Modo Treino Ativo (Live Workout Logger)**
   - Tipos de série: **W** (Aquecimento), **F** (Feeder), **T** (Trabalho), **TOP** (Top Set) e **DROP** (Drop-Set).
   - Controle rápido de **Carga (kg)**, **Repetições** e **RPE**.
   - **Detecção Automática de Recorde Pessoal (PR!)** e cálculo instantâneo de 1RM estimado.
   - **Cronômetro de Descanso Inteligente** com arco circular animado, botões `-15s` / `+15s` e vibração háptica ao zerar.

4. **Enciclopédia Biomecânica (48 Exercícios Completos)**
   - Todos os grupos musculares: Peito, Costas, Ombros, Bíceps, Tríceps, Quadríceps, Posteriores, Glúteos, Panturrilhas e Core.
   - Execução passo a passo, músculos sinergistas, dicas biomecânicas, erros comuns, tempo sob tensão (TUT) e descanso recomendado.

5. **Laboratório de Ciência & Calculadoras**
   - **Calculadora de 1RM** (Fórmulas de Epley e Brzycki + Zonas de Intensidade de 60% a 95%).
   - **Calculadora de Dieta, TMB & Macros** (Mifflin-St Jeor para Bulking Limpo, Manutenção e Cutting).
   - **Analisador de Volume Semanal (MEV / MAV / MRV)** por grupo muscular.
   - **8 Guias Científicos Completos** sobre Sobrecarga Progressiva, RIR, Técnicas Avançadas, Periodização, Nutrição e Suplementação.

---

## 🚀 Como Compilar o APK Automaticamente pelo GitHub Actions

1. Crie um novo repositório no seu **GitHub** (público ou privado).
2. Extraia este arquivo `.zip` e envie todos os arquivos (incluindo a pasta oculta `.github/`) para o repositório.
3. No GitHub, acesse a aba **Actions** do seu repositório.
4. O workflow **`Build Android APK (IronForge Pro)`** iniciará automaticamente (ou clique em *Run workflow*).
5. Ao concluir (~2 minutos), role até a seção **Artifacts** na parte inferior da execução e baixe **`IronForgePro-Debug-APK`**.
6. Descompacte o artifact e instale o arquivo `app-debug.apk` diretamente no seu smartphone Android!
