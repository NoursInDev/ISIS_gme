package garages;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.PrintStream;
import java.util.*;

/**
 * Représente une voiture qui peut être stationnée dans des garages.
 */
@RequiredArgsConstructor
@ToString
public class Voiture {

	@Getter
	@NonNull
	private final String immatriculation;
	@ToString.Exclude
	private final List<Stationnement> myStationnements = new LinkedList<>();

	/**
	 * Fait rentrer la voiture au garage
	 * Précondition : la voiture ne doit pas être déjà dans un garage
	 *
	 * @param g le garage où la voiture va stationner
	 * @throws IllegalStateException Si déjà dans un garage
	 */
	public void entreAuGarage(Garage g) throws IllegalStateException {
		if (estDansUnGarage()) {
			throw new IllegalStateException("La voiture est déjà dans un garage");
		}
		Stationnement s = new Stationnement(this, g);
		myStationnements.add(s);
	}

	/**
	 * Fait sortir la voiture du garage
	 * Précondition : la voiture doit être dans un garage
	 *
	 * @throws IllegalStateException si la voiture n'est pas dans un garage
	 */
	public void sortDuGarage() throws IllegalStateException {
		if (!estDansUnGarage()) {
			throw new IllegalStateException("La voiture n'est pas dans un garage");
		}
		Stationnement dernier = myStationnements.get(myStationnements.size() - 1);
		dernier.terminer();
	}

	/**
	 * Calcule l'ensemble des garages visités par cette voiture
	 *
	 * @return l'ensemble des garages visités par cette voiture
	 */
	public Set<Garage> garagesVisites() {
		Set<Garage> garages = new LinkedHashSet<>();
		for (Stationnement s : myStationnements) {
			garages.add(s.getGarageVisite());
		}
		return garages;
	}

	/**
	 * Détermine si la voiture est actuellement dans un garage
	 *
	 * @return vrai si la voiture est dans un garage, faux sinon
	 */
	public boolean estDansUnGarage() {
		if (myStationnements.isEmpty()) {
			return false;
		}
		Stationnement dernier = myStationnements.get(myStationnements.size() - 1);
		return dernier.estEnCours();
	}

	/**
	 * Pour chaque garage visité, imprime le nom de ce garage suivi de la liste des
	 * stationnements dans ce garage
	 *
	 * @param out l'endroit où imprimer (ex: System.out pour imprimer dans la
	 *            console)
	 */
	public void imprimeStationnements(PrintStream out) {
		Map<Garage, List<Stationnement>> parGarage = new LinkedHashMap<>();
		for (Stationnement s : myStationnements) {
			Garage g = s.getGarageVisite();
			parGarage.computeIfAbsent(g, k -> new LinkedList<>()).add(s);
		}

		for (Map.Entry<Garage, List<Stationnement>> e : parGarage.entrySet()) {
			out.println(e.getKey().toString() + ":");
			for (Stationnement s : e.getValue()) {
				out.println("\t" + s.toString());
			}
		}
	}

}
