from collections import defaultdict
import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import savgol_filter

import numpy as np
import matplotlib.pyplot as plt
from collections import defaultdict

def process_file(file_path):
    data = defaultdict(list)
    try:
        with open(file_path, 'r') as file:
            for line in file:
                parts = line.split()
                if len(parts) == 2:
                    x_val = int(parts[0])  # Longueur du mot
                    y_val = float(parts[1])  # Temps d'insertion
                    data[x_val].append(y_val)
    except Exception as e:
        print(f"Erreur lors de la lecture du fichier {file_path}: {e}")
        return None, None

    x = np.array(sorted(data.keys()))
    y = np.array([np.mean(data[length]) for length in x])
    return x, y

def plot_smooth_curves(file_path1, file_path2):
    """
    Lit les données depuis deux fichiers et génère deux courbes lissées pour comparaison.
    """
    # Traitement des deux fichiers
    x1, y1 = process_file(file_path1)
    x2, y2 = process_file(file_path2)

    if x1 is None or x2 is None:
        print("Impossible de générer les graphiques en raison d'erreurs de lecture.")
        return

    # Tracer le graphique
    plt.figure(figsize=(12, 8))
    plt.plot(x1, y1, label="Courbe lissée - Trie Patricia", color="red")
    plt.scatter(x1, y1, label="Données moyennées - Trie Patricia", color="blue", s=10)
    plt.plot(x2, y2, label="Courbe lissée - Trie Hybrid", color="green")
    plt.scatter(x2, y2, label="Données moyennées - Trie Hybrid", color="orange", s=10)

    plt.xlabel("Nombre de nœuds")
    plt.ylabel("Temps d'insertion (ms)")
    plt.title("Comparaison des temps pour la fonction de listeMots en fonction du nombre de nœuds dans l'arbre")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 3:
        print("Usage: python script.py <chemin_du_fichier1> <chemin_du_fichier2>")
        sys.exit(1)

    file_path1 = sys.argv[1]
    file_path2 = sys.argv[2]
    plot_smooth_curves(file_path1, file_path2)




