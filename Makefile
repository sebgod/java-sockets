ANT:=ant
CP:=cp

ANT_DIRS:=rocksaw vserv-tcpip

MAKE_ANT_DIR=pushd $(1) ; $(ANT) -Djni.make="$(MAKE)" -Djni.cc="$(CC)" $(2) ; popd

.PHONY: all
all: rocksaw

.PHONY: rocksaw
rocksaw: vserv-tcpip
	@$(CP) -u $</lib/*.jar $@/lib/
	@$(call MAKE_ANT_DIR,$@,all)

.PHONY: vserv-tcpip
vserv-tcpip:
	@$(call MAKE_ANT_DIR,$@,all)

.PHONY: clean
clean:
	@$(foreach ANT_DIR,$(ANT_DIRS),$(call MAKE_ANT_DIR,$(ANT_DIR),clean);)

