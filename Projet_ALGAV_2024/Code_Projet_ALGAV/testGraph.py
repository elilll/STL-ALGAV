import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import savgol_filter
from collections import defaultdict

def plot_smooth_curve(file_path):
    """
    Lit les données depuis un fichier et génère une courbe lissée en traitant les duplications de x.
    """
    # Lecture des données depuis le fichier
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
        print(f"Erreur : {e}")
        return
        

    datas = []

    """for dat in data.keys():
        if (dat % 100 == 0) : 
            datas.append(dat)"""
    # Moyenne des temps pour chaque longueur unique
    #x = np.array(sorted(datas))
    x = np.array(sorted(data.keys()))
    y = np.array([np.mean(data[length]) for length in x])

    # Application du filtre de Savitzky-Golay pour lisser les données
    y_smooth = savgol_filter(y, window_length=7, polyorder=2)  # Paramètres du filtre : à ajuster selon besoin

    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.plot(x, y_smooth, label="Courbe lissée", color="red")
    plt.scatter(x, y, label="Données moyennées", color="blue", s=10)
    plt.xlabel("Longueur du mot (nb de caractères)")
    plt.ylabel("Temps d'insertion (ms)")
    plt.title("Graphe représentant le temps d'insertion d'un mot en fonction de son nombre de caractères")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        print("Usage: python script.py <chemin_du_fichier>")
        sys.exit(1)

    file_path = sys.argv[1]
    plot_smooth_curve(file_path)
