if (e.getView().getTitle().equals("§aUpgrady")) {
    e.setCancelled(true);

    String f = manager.getCompany(p.getUniqueId());

    if (e.getSlot() == 13) {

        if (manager.getBalance(f) >= 1000) {
            manager.removeBalance(f, 1000);
            manager.upgradeMultiplier(f);

            p.sendMessage("§aUpgrade koupen!");
        } else {
            p.sendMessage("§cNedostatek peněz!");
        }
    }
}
