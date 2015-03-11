ANT:=ant
CP:=cp
MKDIR:=mkdir
EXTRA_ANT_FLAGS:=

ifeq ($(WINDIR),)
    CC:=cc
else
    CC:=cl
    EXTRA_ANT_FLAGS+=-Djni.makefile=Makefile.win32
endif

ANT_FLAGS:= -Djni.make="$(MAKE)" -Djni.cc="$(CC)" $(EXTRA_ANT_FLAGS)
ANT_DIRS:=rocksaw vserv-tcpip

MAKE_ANT_DIR=pushd $(1) ; $(ANT) $(ANT_FLAGS) $(2) ; popd

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

