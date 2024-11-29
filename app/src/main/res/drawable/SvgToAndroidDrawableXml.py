import os
import subprocess

# Dossier contenant les SVG
origin_folder = "C:\\Users\\pauls\\AndroidStudioProjects\\SudokuWave\\app\\src\\main\\res\\drawable"
drawable_folder = "C:\\AndroidVectorDrawable"


# Vérifie l'existence du dossier
if not os.path.exists(origin_folder):
    print(f"Le dossier {origin_folder} n'existe pas.")
    exit(1)

# Récupère tous les fichiers SVG
svg_files = [f for f in os.listdir(origin_folder) if f.endswith(".svg")]

for svg_file in svg_files:
    input_path = os.path.join(origin_folder, svg_file)
    output_path = os.path.join(drawable_folder, svg_file[:-4] + ".xml")
    print(f"Fichier de sortie attendu : {output_path}")

    subprocess.run([
    "inkscape",
    input_path,
    "--export-plain-svg", output_path  # Export en SVG simplifié
])


print("Conversion terminée !")
