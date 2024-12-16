from collections import defaultdict
import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import savgol_filter

def plot_smooth_curves(file_path1, file_path2):
    """
    Lit les données depuis deux fichiers et génère deux courbes lissées pour comparaison.
    """
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

    # Traitement des deux fichiers
    x1, y1 = process_file(file_path1)
    x2, y2 = process_file(file_path2)

    if x1 is None or x2 is None:
        print("Impossible de générer les graphiques en raison d'erreurs de lecture.")
        return

    # Lissage des courbes avec le filtre de Savitzky-Golay
    y1_smooth = savgol_filter(y1, window_length=7, polyorder=2)
    y2_smooth = savgol_filter(y2, window_length=7, polyorder=2)

    # Tracer le graphique
    plt.figure(figsize=(12, 8))
    plt.plot(x1, y1_smooth, label="Courbe lissée - Trie Patricia", color="red")
    plt.scatter(x1, y1, label="Données moyennées - Trie Patricia", color="blue", s=10)
    plt.plot(x2, y2_smooth, label="Courbe lissée - Trie Hybrid", color="green")
    plt.scatter(x2, y2, label="Données moyennées - Trie Hybrid", color="orange", s=10)

    plt.xlabel("Longueur du mot (nb de caractères)")
    plt.ylabel("Temps d'insertion (ms)")
    plt.title("Comparaison des temps d'insertion en fonction de la longueur des mots")
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

