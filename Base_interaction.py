import string

commandes = ["ls", "mkdir", "cd", "download", "upload" ]

def testerValiditeCommandes(commande):
    if commande not in commandes:
        print("Commande invalide")
        return False
    return True


class Dossier:
    def __init__(self, nom = "", parent = None) -> None:
        self.contenu = []
        self.parent = parent
        self.nom = nom
        pass

    def __str__(self) -> str:
        return self.nom
    
    def __repr__(self) -> str:
        return str(self)

    def __eq__(self, __o: object) -> bool:
        if type(__o) == string:
            return self.nom == __o
        elif type(__o) == Dossier:
            return self.nom == __o.nom and self.parent == __o.parent
        pass
    

class Fichier:
    def __init__(self, nom, taille) -> None:
        self.nom = nom
        self.taille = taille
        pass

    def __str__(self) -> str:
        return self.nom
    
    def __repr__(self) -> str:
        return str(self)

    def __eq__(self, __o: object) -> bool:
        if type(__o) == string:
            return self.nom == __o
        elif type(__o) == Fichier:
            return self.nom == __o.nom and self.taille == __o.taille
        pass

class Serveur:
    def __init__(self) -> None:
        self.dossier = Dossier()
        self.dossierActuel = self.dossier
        pass

    def mkdir(self, nom):
        if nom in self.dossierActuel.contenu:
            print("Ce dossier existe deja")
        else:
            self.dossierActuel.contenu.append(Dossier(nom, self))

    def ls(self):
        for element in self.dossierActuel.contenu:
            print(element)

    def cd(self, cible):
        if cible == "..":
            self.dossierActuel = self.dossierActuel.parent
        elif cible not in self.dossierActuel.contenu:
            print("Aucun dossier de ce nom")
        else:
            self.dossierActuel = self.dossierActuel.contenu[self.dossierActuel.contenu.index(cible)]

    def upload(self, nom, taille):
        if nom in self.dossierActuel.contenu:
            self.dossierActuel.contenu.remove(nom)
        self.dossierActuel.contenu.append(Fichier(nom,taille))

    def download(self, nom):
        if nom in self.dossierActuel.contenu:
            return self.dossierActuel.contenu[self.contenu.index(nom)]
        else:
            print("Aucun fichier de ce nom existe")


class Client:
    commandes = ["ls", "mkdir", "cd", "download", "upload"]

    def __init__(self) -> None:
        self.dossier = Dossier()
        self.dossierActuel = self.dossier
        pass

    def validerCommande(self, commande):
        if commande.split()[0] in Client.commandes:
            return True
        else:
            print("Commande invalide")
            return False

    def validerUpload(self, commande):
        nom = ' '.join(commande.split().pop(0))
        if nom in Client.commandes:
            return True
        else:
            print("Aucun fichier de ce nom dans le dossier actuel")

