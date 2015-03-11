ANT:=ant
CP:=cp
MKDIR:=mkdir

ANT_DIRS:=rocksaw vserv-tcpip

MAKE_ANT_DIR=pushd $(1) ; $(ANT) -Djni.make="$(MAKE)" -Djni.cc="$(CC)" $(2) ; popd

.PHONY: all
all: rocksaw

.PHONY: rocksaw
rocksaw: vserv-tcpip
	@$(MKDIR) -p $@/lib
	@$(CP) -t $@/lib -u $</lib/*.jar
	@$(call MAKE_ANT_DIR,$@,all)

.PHONY: vserv-tcpip
vserv-tcpip:
	@$(call MAKE_ANT_DIR,$@,all)

.PHONY: clean
clean:
	@$(foreach ANT_DIR,$(ANT_DIRS),$(call MAKE_ANT_DIR,$(ANT_DIR),clean);)

